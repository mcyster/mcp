# Repo Guidelines for Codex

## Nixos 
Nixos is the default build environment

If you are on nixos or have the nix package manager avaliable

```bash
nix-channel --add https://nixos.org/channels/nixpkgs-unstable nixpkgs
nix-channel --update
```

Use `nix-shell shell.nix` to enter the development environment. **All** `gradle` commands must run inside this shell.

## Ubuntu 
Ubuntu can be used as a development envioment

```bash
sudo apt-get update
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

