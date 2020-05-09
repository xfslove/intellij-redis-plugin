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
public class Connection implements Cloneable {

  private String name;
  private String url;
  private boolean savePassword;

  private String passwordCache;
  private boolean cluster;

  public String getUniName() {
    return name + "/" + url;
  }

  public boolean isCluster() {
    return cluster;
  }

  public void setCluster(boolean cluster) {
    this.cluster = cluster;
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

  public void storePasswordCache(String passwordCache) {
    this.passwordCache = passwordCache;
  }

  public String retrievePasswordCache() {
    return passwordCache;
  }

  public void removePasswordFromOs() {
    if (!isSavePassword()) {
      return;
    }
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", getUniName()));
    PasswordSafe.getInstance().set(credentialAttributes, null);
  }

  public void storePasswordToOs() {
    if (!isSavePassword()) {
      return;
    }
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", getUniName()));
    Credentials credentials = new Credentials(url, passwordCache);
    PasswordSafe.getInstance().set(credentialAttributes, credentials);
  }

  public String retrievePasswordFromOs() {
    CredentialAttributes credentialAttributes =
        new CredentialAttributes(CredentialAttributesKt.generateServiceName("redis-connections", getUniName()));
    Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
    if (credentials != null) {
      return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }
    return null;
  }

  public String retrievePassword() {

    String passwordCache = retrievePasswordCache();
    if (!isSavePassword()) {
      return passwordCache;
    }

    if (StringUtils.isNotBlank(passwordCache)) {
      return passwordCache;
    }
    return retrievePasswordFromOs();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return savePassword == that.savePassword &&
        cluster == that.cluster &&
        name.equals(that.name) &&
        url.equals(that.url) &&
        Objects.equals(passwordCache, that.passwordCache);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url, savePassword, passwordCache, cluster);
  }
}
