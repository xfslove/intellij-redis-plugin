package com.github.xfslove.intellij.plugin.redis.storage;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class Configuration implements Cloneable {

  private String name;
  private String url;
  private boolean savePassword;

  private String oneTimePassword;

  public boolean isCluster() {
    return url.split(",").length > 1;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isSavePassword() {
    return savePassword;
  }

  public void setSavePassword(boolean savePassword) {
    this.savePassword = savePassword;
  }

  public void oneTimePassword(String oneTimePassword) {
    this.oneTimePassword = oneTimePassword;
  }

  public void removePasswordFromOs() {
    if (!savePassword) {
      return;
    }
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", url));
    PasswordSafe.getInstance().set(credentialAttributes, null);
  }

  public void storePasswordToOs() {
    if (!savePassword) {
      return;
    }
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", url));
    Credentials credentials = new Credentials(url, oneTimePassword);
    PasswordSafe.getInstance().set(credentialAttributes, credentials);
  }

  public String retrievePasswordFromOs() {
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", url));
    Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
    if (credentials != null) {
      return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }
    return null;
  }

  public String retrievePassword() {

    if (!savePassword) {
      return oneTimePassword;
    }

    if (StringUtils.isNotBlank(oneTimePassword)) {
      return oneTimePassword;
    }
    return retrievePasswordFromOs();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Configuration that = (Configuration) o;
    return savePassword == that.savePassword &&
        name.equals(that.name) &&
        url.equals(that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url, savePassword);
  }
}
