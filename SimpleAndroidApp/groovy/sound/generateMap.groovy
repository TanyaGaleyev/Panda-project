package sound;

def base = args[0]
def mainPackage = "com.pavlukhin.acropanda"
def codePackage = mainPackage + ".game.sound"
def destFile = new File("$base/generated/" + codePackage.replace(".", "/") + "/SoundMap.java")
destFile.getParentFile().mkdirs()
destFile.write("")
destFile << "package " + codePackage + ";\n"
destFile << "\n"
destFile << "import android.content.Context;\n"
destFile << "import android.media.SoundPool;\n"
destFile << "import android.util.SparseArray;\n"
destFile << "\n"
destFile << "import " + mainPackage + ".R;\n"
destFile << "\n"
destFile << "/** Auto generated " + new Date() + " */\n"
destFile << "public class SoundMap {\n"
destFile << "    private SparseArray<Integer> map = new SparseArray<Integer>();\n"
destFile << "\n"
destFile << "    public SoundMap(Context context, SoundPool soundPool) {\n"
for(File soundRes : new File("$base/res/raw").listFiles({d, f-> f ==~ /.*.wav/ } as FilenameFilter)) {
    def resName = soundRes.getName();
    def resId = "R.raw." + resName.substring(0, resName.length() - 4);
    destFile << "        map.put($resId, soundPool.load(context, $resId, 1));\n"
}
destFile << "    }\n"
destFile << "\n"
destFile << "    public int get(int resId) {\n"
destFile << "        return map.get(resId);\n"
destFile << "    }\n"
destFile << "}"