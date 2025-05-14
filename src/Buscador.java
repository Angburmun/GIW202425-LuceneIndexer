import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Paths;

public class Buscador extends JFrame {

    private JTextField queryField;
    private JTextArea resultArea;
    private String indexPath;

    public Buscador(String indexPath) {
        super("Buscador Lucene");
        this.indexPath = indexPath;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Búsqueda
        JPanel topPanel = new JPanel(new BorderLayout());
        queryField = new JTextField();
        JButton searchButton = new JButton("Buscar");

        searchButton.addActionListener(this::buscar);
        topPanel.add(queryField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // Resultados
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void buscar(ActionEvent e) {
        try {
            DirectoryReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);
            StoredFields storedFields = searcher.storedFields();
            Analyzer analyzer = new EnglishAnalyzer();

            QueryParser parser = new QueryParser("contents", analyzer);
            Query query = parser.parse(queryField.getText());

            TopDocs results = searcher.search(query, 10);
            resultArea.setText("");

            for (ScoreDoc hit : results.scoreDocs) {
                Document doc = storedFields.document(hit.doc);
                String filename = doc.get("filename");
                float score = hit.score;
                resultArea.append(String.format("📄 %s (Score: %.2f)\n", filename, score));
            }

            reader.close();
        } catch (Exception ex) {
            resultArea.setText("⚠️ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Buscador <ruta_index>");
            return;
        }
        SwingUtilities.invokeLater(() -> new Buscador(args[0]));
    }
}
