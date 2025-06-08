# Repo Guidelines for Codex

## Nix Setup

To build or test this project, first install Nix and update channels:

```bash
apt-get update && apt-get install -y nix-bin
nix-channel --add https://nixos.org/channels/nixpkgs-unstable nixpkgs
nix-channel --update
```

Use `nix-shell shell.nix` to enter the development environment. **All** `gradle` commands must run inside this shell.

## Gradle Commands

Common commands run inside the shell:

- `gradle test`
- `gradle :mcp-app:bootJar`
- `gradle :mcp-server:runMcpTestSse`
- `gradle :mcp-server:runMcpTestStdio`

These are the test commands used locally and should be executed within the Nix environment.

## Ubuntu Note

If you are using Ubuntu and need to install the Java and Gradle tools manually,
`UBUNTU_SETUP.md` contains a sample script that approximates the `shell.nix`
configuration. Building and testing this project still requires running the
commands inside `nix-shell shell.nix`.
