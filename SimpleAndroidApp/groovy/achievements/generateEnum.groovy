/**
 * Created by ivan on 10.11.13.
 */
def codePackage = "org.ivan.simple.achievements";
def enumFile = new File("generated/" + codePackage.replace(".", "/") + "/Achievement.java");
enumFile.getParentFile().mkdirs();
enumFile.write("");
enumFile << "package " + codePackage + ";\n";
enumFile << "\n";
enumFile << "/** Auto generated " + new Date() + " */\n";
enumFile << "public enum Achievement {\n";
def strBuilder = new StringBuilder();
for(File prop : new File("groovy/achievements/info").listFiles()) {
    strBuilder << prepareSingleValue(prop);
}
enumFile << (strBuilder.delete(strBuilder.length() - 2, strBuilder.length()) << ";\n").toString();
enumFile << "    public final String title;\n";
enumFile << "    public final String description;\n";
enumFile << "    private Achievement(String title, String description) {\n";
enumFile << "         this.title = title;\n";
enumFile << "         this.description = description;\n";
enumFile << "    }\n"
enumFile << "}\n";

def String prepareSingleValue(File propsFile) {
    Properties props = new Properties();
    props.load(propsFile.newInputStream());
    return sprintf("    %s(%s, %s),\n",
            propsFile.getName().toUpperCase(),
            props.getProperty("title"),
            props.getProperty("description")
    );
}