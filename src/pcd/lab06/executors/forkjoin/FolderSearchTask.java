package pcd.lab06.executors.forkjoin;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Long> {
    private final Folder folder;
    private final String searchedWord;
    private final WordCounter wc;
    
    public FolderSearchTask(WordCounter wc, Folder folder, String searchedWord) {
        super();
        this.wc = wc;
        this.folder = folder;
        this.searchedWord = searchedWord;
    }
    
    @Override
    protected Long compute() {
        long count = 0L;
        // dato un folder va a controllare i subfolder e lancia il nuovo tasks
        List<RecursiveTask<Long>> forks = new LinkedList<RecursiveTask<Long>>();
        for (Folder subFolder : folder.getSubFolders()) {
            FolderSearchTask task = new FolderSearchTask(wc, subFolder, searchedWord);
            forks.add(task);
            // e chiamo il fork su quel task, in questo modo l'executor di questo task va a prendere in carico il task
            task.fork();
        }
        
        for (Document document : folder.getDocuments()) {
            // per ogni documento va a cercare il conteggio
            DocumentSearchTask task = new DocumentSearchTask(wc, document, searchedWord);
            forks.add(task);
            task.fork();
        }

        // fase di reduce
        for (RecursiveTask<Long> task : forks) {
            // il risultato è il numero di volte che la parola compare e va ad fare una join,
            // la join è come un callable infatti è parametrizzata a long (vedi extends RecursiveTask<Long>)
            count = count + task.join();
        }
        return count;
    }
}
    