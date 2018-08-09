package models.shared_models


import java.io.*


/**
 * This class is used to transfer and receive files
 */
class FileTransfer {
    /**
     * get method for transferredFileSize
     * @return the number of bytes that have been written/read so far
     */
    var transferredFileSize: Long = 0
        private set
    private var firstFile: File? = null
    private var work = true

    /**
     * method used to recursively upload files/folders
     * @param outputStreamStrings is output stream for strings
     * @param outputStreamBytes is output stream for bytes
     * @param file is the main file/folder to be uploaded
     * @param mainPath is the parents path (to establish relationship of files)
     */
    fun sendFiles(outputStreamStrings: DataOutputStream,
                  outputStreamBytes: DataOutputStream,
                  file: File, mainPath: String) {

        try {
            //check if pipe was broken
            if (!work)
                return

            val basicFileData = BasicFileData(file)

            basicFileData.path = basicFileData.path!!.substring(mainPath.length)


            if (basicFileData.path!!.startsWith("\\") || basicFileData.path!!.startsWith("/"))
                basicFileData.path = basicFileData.path!!.substring(1)

            val jsonFile = JsonParser.basicFileDataToJson(Array<BasicFileData?>(1) {basicFileData })

            outputStreamStrings.write(jsonFile.toByteArray(charset("UTF-8")))

            outputStreamStrings.writeBytes("\n")

            outputStreamStrings.flush()

            if (file.isDirectory) {
                val list = file.listFiles()
                for (i in list!!.indices)
                    sendFiles(outputStreamStrings, outputStreamBytes, list[i], mainPath)
            } else {
                val fileData = FileInputStream(file)
                val buffer = ByteArray(BUFFER)
                var size = file.length()
                while (size != 0L) {
                    if (size >= buffer.size) {
                        fileData.read(buffer, 0, buffer.size)
                        outputStreamBytes.write(buffer, 0, buffer.size)
                        size -= buffer.size.toLong()
                        this.transferredFileSize += buffer.size.toLong()
                    } else {
                        fileData.read(buffer, 0, size.toInt())
                        outputStreamBytes.write(buffer, 0, size.toInt())
                        this.transferredFileSize += size
                        size = 0
                    }
                    outputStreamBytes.flush()
                }
                fileData.close()
            }
        } catch (e: Exception) {
            work = false //pipe was broken
            e.printStackTrace()
        }

    }

    /**
     * method used to download files/folders
     * @param byteStream is input stream to receive bytes from
     * @param inputStream is input stream to receive strings from
     * @param path is location to save data under
     */
    fun receiveFiles(byteStream: DataInputStream, inputStream: BufferedReader, path: String) {
        var output: FileOutputStream? = null
        try {
            var temp: String? =inputStream.readLine()
            while (temp != null) {
                val tempBasicFileData = JsonParser.JsonToBasicFileData(temp)
                var basicFileData: BasicFileData?

                if (tempBasicFileData != null)
                    basicFileData = tempBasicFileData[0]
                else
                    throw Exception("No Meta Data")

                if (basicFileData!!.isDirectory) {
                    val file = File(path + basicFileData.path!!)

                    //check if file is the first to be received
                    if (firstFile == null)
                        firstFile = file

                    file.mkdirs()
                } else {

                    output = FileOutputStream(path + basicFileData.path!!)
                    var size = basicFileData.size
                    val buffer = ByteArray(BUFFER)

                    //check if file is the first to be received
                    if (firstFile == null)
                        firstFile = File(path + basicFileData.path!!)

                    while (size > 0) {
                        val bytesRead: Int = if (size > BUFFER)
                                                byteStream.read(buffer, 0, BUFFER)
                                             else
                                                byteStream.read(buffer, 0, size.toInt())

                        if (bytesRead != -1) {
                            this.transferredFileSize += bytesRead.toLong()
                            size -= bytesRead.toLong()
                            output.write(buffer, 0, bytesRead)
                        } else
                            break

                    }
                    output.close()
                }
                temp = inputStream.readLine()
            }
        } catch (e: Exception) {
            try {
                output!!.close()
            } catch (e1: IOException) {
            }

            deleteFile(firstFile)
            e.printStackTrace()
        }

    }

    /**
     * This method is used to calculate Total size for files/folders
     * @param file is a file/folder
     * @return the total size of the file/folder
     */
    fun calculateSize(file: File): Long {
        var sum: Long = 0
        if (file.isDirectory) {
            for (temp in file.listFiles()!!) {
                if (temp.isDirectory)
                    sum += calculateSize(temp)
                else
                    sum += temp.length()
            }
        } else {
            sum += file.length()
        }
        return sum
    }

    companion object {

        private val BUFFER = 1024 * 1024 * 4


        /**
         * method used to delete a file/folder
         * @param file is file/folder to be deleted
         */
        private fun deleteFile(file: File?) {
            if (file == null || !file.exists())
                return
            try {
                if (file.isDirectory) {
                    for (sub in file.listFiles()!!) {
                        if (sub.isDirectory)
                            deleteFile(sub)
                        sub.delete()
                    }
                }
                file.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
