package sdk.util

import java.io.File
import java.io.FileNotFoundException

class Operations{
    companion object {
        fun getFileContent(fileName: String){
            try {
                File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)
            }catch (e : FileNotFoundException){
                println("File not found ($fileName)")
                e.printStackTrace()
            }
        }
    }
}