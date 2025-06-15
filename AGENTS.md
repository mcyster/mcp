# Repo Guidelines for Codex

## Nixos 
Nixos is the default build environment

To build or test this project, first install Nix and update channels:

```bash
apt-get update && apt-get install -y nix-bin
nix-channel --add https://nixos.org/channels/nixpkgs-unstable nixpkgs
nix-channel --update
```

You can optionally enter a Nix shell using `nix-shell shell.nix`, but on Ubuntu
the tools can be run directly without it.

## Ubuntu 
If you are working under Ubuntu thees  steps approximate the dependencies declared in `shell.nix`.

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

Common commands run from the project root:

- `gradle test`
- `gradle :mcp-app:bootJar`
- `gradle :mcp-server:runMcpTestSse`
- `gradle :mcp-server:runMcpTestStdio`

These are the test commands used locally.

### Process

Before submitting a code review always run

```
gradle test
gradle spotlessCheck 
```
