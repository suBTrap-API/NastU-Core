package subtrap.nastu.objects;

import java.io.File;

public record VKAttach(String file) {

    public File toFile(){
        return new File(this.file);
    }

    public File toFile(String path) {
        return new File(path, this.file);
    }

    public static VKAttach fromFile(File file) {
        return new VKAttach(file.getAbsolutePath());
    }
}
