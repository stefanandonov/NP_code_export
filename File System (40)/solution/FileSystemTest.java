//package prv_kolokvium_2019;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



interface IFile extends Comparable<IFile> {

    String getFileName();

    long getFileSize();

    String getFileInfo(int indent);

    void sortBySize();

    long findLargestFile();

}

class IndentPrinter {
    public static String printIndent(int indentLevel) {
        return IntStream.range(0, indentLevel)
                .mapToObj(i -> "\t")
                .collect(Collectors.joining());
    }
}

class FileNameExistsException extends Exception {

    FileNameExistsException(String fileName, String folderName) {
        super(String.format("There is already a file named %s in the folder %s",
                fileName,
                folderName)
        );
    }
}

class File implements IFile {

    protected String fileName;
    protected long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public File(String fileName) {
        this.fileName = fileName;
        this.fileSize = 0L;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileInfo(int indent) {
        return String.format("%sFile name: %10s File size: %10d\n",
                IndentPrinter.printIndent(indent),
                getFileName(),
                getFileSize());
    }

    @Override
    public void sortBySize() {
        return ;
    }

    @Override
    public long findLargestFile() {
        return this.fileSize;
    }

    @Override
    public int compareTo(IFile iFile) {
        return Long.compare(this.getFileSize(),iFile.getFileSize());
    }
}

class Folder extends File implements IFile {

    List<IFile> files;

    public Folder(String fileName) {
        super(fileName);
        files = new ArrayList<>();
    }

    private boolean checkNameExistence(String filename) {
        return files.stream()
                .map(IFile::getFileName)
                .anyMatch(name -> name.equals(filename));
    }

    public void addFile (IFile file) throws FileNameExistsException {
        if (checkNameExistence(file.getFileName())) {
            throw new FileNameExistsException(file.getFileName(), this.fileName);
        }

        files.add(file);
    }

//    public void addFile(String fileName, long fileSize) throws FileNameExistsException {
//
//        if (checkNameExistence(fileName))
//            throw new FileNameExistsException(fileName, this.fileName);
//
//        files.add(new File(fileName, fileSize));
//    }
//
//    public void addFolder(Folder folder) throws FileNameExistsException {
//
//        if (checkNameExistence(folder.fileName))
//            throw new FileNameExistsException(folder.fileName, this.fileName);
//
//        files.add(folder);
//    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                IndentPrinter.printIndent(indent),
                fileName,
                this.getFileSize()));

        files.stream().forEach(file -> sb.append(file.getFileInfo(indent + 1)));

        return sb.toString();
    }


    @Override
    public long findLargestFile() {

        OptionalLong largestHere = files.stream().mapToLong(IFile::findLargestFile).max();

//        OptionalLong largestHere = files.stream()
//                .mapToLong(IFile::getFileSize)
//                .max();

//        List<Long> maximums = new ArrayList<>();
//        for (IFile iFile : files) {
//            if (iFile.getFileType()==FILE_TYPE.FOLDER) {
//                Folder f = (Folder) iFile;
//                maximums.add(f.findLargestFile());
//            }
//        }

//        if (largestHere.isPresent())
//            maximums.add(largestHere.getAsLong());
//
//        OptionalLong result = maximums.stream().mapToLong(i -> i).max();
        if (largestHere.isPresent())
            return largestHere.getAsLong();
        else
            return 0L;
    }

    public void sortBySize() {

        Comparator<IFile> iFileComparator = Comparator.comparingLong(IFile::getFileSize);
        files.sort(iFileComparator);

        files.forEach(IFile::sortBySize);
    }
}

class FileSystem {
    Folder root;

    FileSystem() {
        root = new Folder("root");
    }

    void addFile (IFile file) throws FileNameExistsException {
        root.addFile(file);
    }

//    void addFolder (Folder folder) throws FileNameExistsException {
//        root.addFolder(folder);
//    }
//
//    void addFile (String fileName, long size) throws FileNameExistsException {
//        root.addFile(fileName,size);
//    }

    long findLargestFile() {
        return root.findLargestFile();
    }

    void sortBySize () {
        root.sortBySize();
    }

    @Override
    public String toString() {
        return this.root.getFileInfo(0);
    }
}


public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());

//        File f = new File("test", 10);
//        System.out.println(f.getFileInfo(0));
//        Folder folder = new Folder("folder");
//        folder.addFile("stefan", 100);
//        folder.addFile("stefan1", 15);
//        folder.addFile("stefan123", 22);
//        Folder folder1 = new Folder("folder1");
//        folder1.addFile("test11", 1555);
//        folder1.addFile("test134", 133);
//        folder.addFolder(folder1);
//
//        System.out.println(folder.getFileInfo(0));
//
//        folder.sortBySize();
//
//        System.out.println(folder.getFileInfo(0));
//
//        System.out.println(folder.findLargestFile());




    }
}
