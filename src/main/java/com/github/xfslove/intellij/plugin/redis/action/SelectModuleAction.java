package com.github.xfslove.intellij.plugin.redis.action;

import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wongiven
 * @date created at 2020/4/21
 */
public class SelectModuleAction extends DumbAwareAction {

  public SelectModuleAction() {
    super("Select Module", "Select module", AllIcons.Modules.SourceRoot);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Project project = e.getProject();
    Module[] modules = ModuleManager.getInstance(project).getModules();

    for (Module module : modules) {

      JavaRunConfigurationModule configurationModule = new JavaRunConfigurationModule(project, false);
      configurationModule.setModule(module);

      PsiClass aClass = configurationModule.findClass("A");
      PsiMethod[] constructors = aClass.getConstructors();

      CompilerManager.getInstance(module.getProject())
          .compile(module, (aborted, errors, warnings, compileContext) -> {

            if (!aborted && errors == 0) {
              String outputUrl = CompilerModuleExtension.getInstance(module).getCompilerOutputUrl();
              System.out.println(outputUrl);

              final List<String> list = new ArrayList<>();
//                OrderEnumerator.orderEntries(module).recursively().runtimeOnly().getPathsList().getPathList();
              list.add(outputUrl);

              final List<URL> urls = new ArrayList<>();
              for (String path : list) {
                try {
                  urls.add(new File(FileUtil.toSystemIndependentName(path)).toURI().toURL());
                } catch (MalformedURLException e1) {
                  e1.printStackTrace();
                }
              }

              UrlClassLoader loader = UrlClassLoader.build().parent(ClassLoader.getSystemClassLoader()).urls(urls).get();

              try {
                Class<?> a = loader.loadClass(aClass.getQualifiedName());

              } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
              }
            }

          });

//      Class<?> a = generateAndShowXml(module, "A");

//      System.out.println(a.toString());


    }
  }

}
