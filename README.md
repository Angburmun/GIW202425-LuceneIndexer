# GIW202425-LuceneIndexer
Indexador Java con Lucene.

## ESTRUCTURA:
/LuceneIndexer/
├── indexador.jar
├── buscador.jar/
├── docs/
│   └── (archivos .txt)
└── index/

## USO:
`java -jar indexador.jar <carpeta-docs> <carpeta-index>` -> Indexa los archivos de la carpeta docs dentro de la carpeta index.

`java -jar buscador.jar <carpeta-index>` -> Busca las coincidencias dentro de la carpeta index.
