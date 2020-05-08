package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.client.RedisClient;
import com.github.xfslove.intellij.plugin.redis.client.RedisConnectionHolder;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.github.xfslove.intellij.plugin.redis.storage.ConnectionStorage;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.lang.UrlClassLoader;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

      if (!"demo".equals(module.getName())) {
        continue;
      }

//      JavaRunConfigurationModule configurationModule = new JavaRunConfigurationModule(project, false);
//      configurationModule.setModule(module);
//
//      PsiClass psiClass = configurationModule.findClass("com.example.demo.B");

      CompilerManager.getInstance(module.getProject())
          .compile(module, (aborted, errors, warnings, compileContext) -> {

            if (!aborted && errors == 0) {
              List<String> list = OrderEnumerator.orderEntries(module).recursively().compileOnly().getPathsList().getPathList();

              List<URL> urls = new ArrayList<>();
              for (String path : list) {
                try {
                  urls.add(new File(FileUtil.toSystemIndependentName(path)).toURI().toURL());
                } catch (MalformedURLException e1) {
                  e1.printStackTrace();
                }
              }

              UrlClassLoader loader = UrlClassLoader.build().parent(ClassLoader.getSystemClassLoader()).urls(urls).get();

              JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer(loader);

              RedisConnectionHolder client = ServiceManager.getService(project, RedisConnectionHolder.class);

              ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);

              Connection connection = storage.getConnections().get(0);

              RedisClient redisClient = client.getClient(connection);

              String key = "test";
              try {
                byte[] result = redisClient.get(key.getBytes(StandardCharsets.UTF_8));

                Object o = serializer.deserialize(result);

                System.out.println(o);

              } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
              } catch (ExecutionException executionException) {
                executionException.printStackTrace();
              } catch (Exception exception) {
                exception.printStackTrace();
              }


            }

          });


    }
  }

}
