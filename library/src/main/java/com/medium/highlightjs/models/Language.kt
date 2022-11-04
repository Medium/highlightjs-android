package com.medium.highlightjs.models

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */
enum class Language(val languageName: String?) {
    AUTO_DETECT(null),
    DISABLE_HIGHLIGHT("nohighlight"),
    _1C("1c"),
    ABNF("abnf"),
    ACCESS_LOGS("accesslog"),
    ADA("ada"),
    ARM_ASSEMBLER(
        "arm"
    ),
    AVR_ASSEMBLER("avrasm"),
    ACTION_SCRIPT("actionscript"),
    ANGLE_SCRIPT("anglescript"),
    APACHE("apache"),
    APPLE_SCRIPT("applescript"),
    ASCII_DOC(
        "asciidoc"
    ),
    ASPECT_J("aspectj"),
    AUTO_HOTKEY("autohotkey"),
    AUTO_IT("autoit"),
    AXAPTA("axapta"),
    AWK("awk"),
    BASH("bash"),
    SHELL("sh"),
    ZSH(
        "zsh"
    ),
    BASIC("basic"),
    BNF("bnf"),
    BRAINFUCK("brainfuck"),
    C("c"),
    C_SHARP("csharp"),
    C_PLUS_PLUS("cpp"),
    C_AL("cal"),
    CACHE_OBJECT_SCRIPT(
        "cos"
    ),
    C_MAKE("cmake"),
    COQ("coq"),
    CSP("csp"),
    CSS("css"),
    CAPTAIN_PROTO("capnproto"),
    CHAOS("chaos"),
    CISCO_CLI("cisco"),
    CLEAN("clean"),
    CLOJURE(
        "clojure"
    ),
    COFFEE_SCRIPT("coffeescript"),
    CPCDOSC_PLUS("cpc"),
    CRMSH("crmsh"),
    CRYSTAL("crystal"),
    CYPHER_NEO4J("cypher"),
    D("d"),
    DNS_ZONE_FILE(
        "dns"
    ),
    DOS("dos"),
    BATCH("bat"),
    DART("dart"),
    DELPHI("delphi"),
    DIFF("diff"),
    DJANGO("django"),
    DOCKER_FILE("dockerfile"),
    DSCONFIG(
        "dsconfig"
    ),
    DTS("dts"),
    DUST("dust"),
    DYLAN("dylan"),
    EBNF("ebnf"),
    ELIXIR("elixir"),
    ELM("elm"),
    ERLANG("erlang"),
    EXCEL("excel"),
    F_SHARP(
        "fsharp"
    ),
    FIX("fix"),
    FLIX("flix"),
    FORTRAN("fortran"),
    G_CODE("gcode"),
    GAMS("gams"),
    GAUSS("gauss"),
    GDSCRIPT("godot"),
    GHERKIN("gherkin"),
    GN_FOR_NINJA(
        "gn"
    ),
    GO("go"),
    GRAPHQL("graphql"),
    GRAMMATICAL_FRAMEWORK("gf"),
    GOLO("golo"),
    GRADLE("gradle"),
    GROOVY("groovy"),
    HTML("html"),
    XML("xml"),
    HTTP("http"),
    HAML(
        "haml"
    ),
    HANDLEBARS("hbs"),
    HASKELL("hs"),
    HAXE("hx"),
    HY("hy"),
    INI("ini"),
    INFORM7("i7"),
    IRPF90("irpf90"),
    JSON("json"),
    JAVA("java"),
    JAVA_SCRIPT(
        "javascript"
    ),
    JOLIE("ol"),
    KOTLIN("kt"),
    LATEX("tex"),
    LASSO("lasso"),
    LEAF("leaf"),
    LEAN("lean"),
    LESS("less"),
    LDIF("ldif"),
    LISP("lisp"),
    LIVE_CODE_SERVER(
        "livecodeserver"
    ),
    LIVE_SCRIPT("livescript"),
    LLVM("llvm"),
    LUA("lua"),
    MAKEFILE("makefile"),
    MARKDOWN("md"),
    MATHEMATICA("mma"),
    MATLAB("matlab"),
    MAXIMA(
        "maxima"
    ),
    MAYA_EMBEDDED_LANGUAGE("mel"),
    MERCURY("mercury"),
    MIRC_SCRIPTING_LANGUAGE("mrc"),
    MIZAR("mizar"),
    MOJOLICIOUS("mojolicious"),
    MONKEY(
        "monkey"
    ),
    MOONSCRIPT("moonscript"),
    N1QL("n1ql"),
    NSIS("nsis"),
    NGINX("nginx"),
    NIMROD("nimrod"),
    NIX("nix"),
    OBJECTIVE_CONSTRAINT_LANGUAGE(
        "ocl"
    ),
    O_CAML("ocaml"),
    OBJECTIVE_C("objectivec"),
    OPENGL_SHADING_LANGUAGE("glsl"),
    OPEN_SCAD("scad"),
    ORACLE_RULES_LANGUAGE("ruleslanguage"),
    OXYGENE(
        "oxygene"
    ),
    PF("pf"),
    PHP("php"),
    PHP_TEMPLATE("php-template"),
    PARSER3("parser3"),
    PERL("perl"),
    PLAIN_TEXT("txt"),
    PONY("pony"),
    POSTGRE_SQL("pgsql"),
    POWER_SHELL("ps"),
    PROCESSING(
        "processing"
    ),
    PROLOG("prolog"),
    PROPERTIES("properties"),
    PROTOCOL_BUFFERS("protobuf"),
    PUPPET("pp"),
    PYTHON("python"),
    PYTHON_PROFILER_RESULTS(
        "profile"
    ),
    PYTHON_REPL("pycon"),
    Q("k"),
    QML("qml"),
    R("r"),
    RAZOR_CSHTML("razor"),
    REASON_ML("re"),
    RENDER_MAN_RIB("rib"),
    RENDER_MAN_RSL(
        "rsl"
    ),
    ROBOCONF("roboconf"),
    RUBY("ruby"),
    RUST("rust"),
    SAS("sas"),
    SCSS("scss"),
    SQL("sql"),
    STEP_PART_21("p21"),
    SCALA("scala"),
    SCHEME(
        "scheme"
    ),
    SCILAB("sci"),
    SMALI("smali"),
    SMALLTALK("smalltalk"),
    STAN("stan"),
    STATA("stata"),
    STYLUS("stylus"),
    SUB_UNIT("subunit"),
    SWIFT(
        "swift"
    ),
    TEST_ANYTHING_PROTOCOL("tap"),
    TCL("tcl"),
    TEX("tex"),
    THRIFT("thrift"),
    TP("tp"),
    TWIG("twig"),
    TYPE_SCRIPT("typescript"),
    VB_NET(
        "vbnet"
    ),
    VB_SCRIPT("vbscript"),
    VHDL("vhdl"),
    VALA("vala"),
    VERILOG("v"),
    VIM("vim"),
    X86_ASSEMBLY("x86asm"),
    XL("xl"),
    X_QUERY("xq"),
    WASM("wasm"),
    YAML("yaml"),
    ZEPHIR(
        "zep"
    );

    companion object {
        val MEDIUM_SUBLIST = listOf(
            BASH,
            C,
            C_SHARP,
            C_PLUS_PLUS,
            CSS,
            DIFF,
            GO,
            GRAPHQL,
            INI,
            HTML,
            JSON,
            JAVA,
            JAVA_SCRIPT,
            KOTLIN,
            LESS,
            LUA,
            XML,
            MAKEFILE,
            MARKDOWN,
            OBJECTIVE_C,
            PHP,
            PHP_TEMPLATE,
            PERL,
            PLAIN_TEXT,
            PYTHON,
            PYTHON_REPL,
            R,
            RUBY,
            RUST,
            SCSS,
            SQL,
            SHELL,
            SWIFT,
            TYPE_SCRIPT,
            VB_NET,
            WASM,
            YAML
        )
    }

}