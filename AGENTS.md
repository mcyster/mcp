# Repo Guidelines for Codex

## Ubuntu 
If you are working under Ubuntu thees  steps approximate the dependencies declared in `shell.nix`.

```bash
sudo apt-get install -y openjdk-21-jdk gradle locales
sudo locale-gen en_US.UTF-8
sudo update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8

# ensure Java 21 is the default
sudo update-alternatives --config java
sudo update-alternatives --config javac

# optional: set JAVA_HOME for the current shell
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
```

## Gradle Commands

Common commands run inside the shell:

- `gradle test`
- `gradle :mcp-app:bootJar`
- `gradle :mcp-server:runMcpTestSse`
- `gradle :mcp-server:runMcpTestStdio`

These are the test commands used locally and should be executed within the Nix environment.

### Process

Before submitting a code review always run

```
gradle test
gradle spotlessCheck 
```

