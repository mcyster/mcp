let
  pkgs = import <nixpkgs> { config = { allowUnfree = true; }; };
in
pkgs.mkShell {
  buildInputs = [
    pkgs.gradle
    pkgs.jdk21
    #pkgs.oauth2l
  ];

  LANG = "en_US.UTF-8";
  LC_ALL = "en_US.UTF-8";
  LOCALE_ARCHIVE = "${pkgs.glibcLocales}/lib/locale/locale-archive";

  shellHook = ''
    export JAVA_HOME=${pkgs.jdk21}
    export PATH=$JAVA_HOME/bin:$PATH

    export MCP_HOME=${builtins.getEnv "PWD"}
    if [ -f $HOME/.mcp-rc ]; then
      source $HOME/.mcp-rc
    fi
    export SPRING_AI_OPENAI_API_KEY=$OPENAI_API_KEY
    export LANG=en_US.UTF-8
  '';
}

