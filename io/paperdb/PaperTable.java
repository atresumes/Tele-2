package io.paperdb;

class PaperTable<T> {
    T mContent;

    PaperTable() {
    }

    PaperTable(T content) {
        this.mContent = content;
    }
}
