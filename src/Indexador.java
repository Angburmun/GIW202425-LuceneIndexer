import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Indexador {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Uso: java Indexador <carpeta_documentos> <carpeta_indices>");
            return;
        }

        String docsPath = args[0];
        String indexPath = args[1];

        Analyzer analyzer = new EnglishAnalyzer();
        FSDirectory dir = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);

        File docsDir = new File(docsPath);
        File[] files = docsDir.listFiles((dir1, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                String content = Files.readString(file.toPath());
                Document doc = new Document();
                doc.add(new TextField("filename", file.getName(), Field.Store.YES));
                doc.add(new TextField("contents", content, Field.Store.YES));
                writer.addDocument(doc);
                System.out.println("Indexado: " + file.getName());
            }
        }

        writer.close();
        System.out.println("Indexación completa.");
    }
}
