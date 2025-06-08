# Ubuntu Setup

These steps approximate the dependencies declared in `shell.nix`.

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

This installs Java 21 and the latest Gradle package available on Ubuntu. Build
and test commands for this repository should still be executed inside
`nix-shell shell.nix` as explained in [AGENTS.md](./AGENTS.md).
