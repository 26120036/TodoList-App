package practice.Todo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TodoManager {
    private final List<Todo> todos;
    private static final String FILE_PATH = "todo.txt";
    private static final String DELIMITER = ",";

    // コンストラクタ
    public TodoManager() {
        this.todos = new ArrayList<>();
    }

    // タスク追加
    public void add(Todo todo) {
        // 重複チェック
        for (Todo t : todos) {
            if (t.getTitle().equals(todo.getTitle())) {
                System.out.println("同じ名前のタスクが既に存在します。");
                return;
            }
        }
        // タスク名に区切り文字が含まれていないかチェック
        if (todo.getTitle().contains(DELIMITER)) {
            System.out.println("タスク名に '" + DELIMITER + "' を含めることはできません。");
            return;
        }

        todos.add(todo);
        save();
        System.out.println("タスクを追加しました。");
    }

    // 一覧表示
    public void show() {
        if (todos.isEmpty()) {
            System.out.println("タスクがありません");
            return;
        }
        System.out.println("No | 状態 | タイトル");
        for (int i = 0; i < todos.size(); i++) {
            Todo t = todos.get(i);
            String status = t.isCompleted() ? "✓" : "□";
            System.out.printf("%2d |  %s   | %s%n", i + 1, status, t.getTitle());
        }
    }

    // タスク完了処理
    public void complete(int number) {
        if (number >= 1 && number <= todos.size()) {
            todos.get(number - 1).complete();
            save();
            System.out.println("タスクを完了にしました。");
        } else {
            System.out.println("無効な番号です。");
        }
    }

    // タスク削除処理
    public void remove(int number) {
        if (number >= 1 && number <= todos.size()) {
            todos.remove(number - 1);
            save();
            System.out.println("タスクを削除しました。");
        } else {
            System.out.println("無効な番号です。");
        }
    }

    // ファイル保存
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, StandardCharsets.UTF_8))) {
            for (Todo t : todos) {
                writer.write(t.getTitle() + DELIMITER + t.isCompleted());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存失敗: " + e.getMessage());
        }
    }

    // ファイル読み込み
    public void load() {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            return; // ファイルがなければ何もしない
        }

        todos.clear();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data.length < 2) continue;

                Todo todo = new Todo(data[0]);
                // trueになってたらcomplete処理実行
                if (Boolean.parseBoolean(data[1])) {
                    todo.complete();
                }
                todos.add(todo);
            }
        } catch (IOException e) {
            System.err.println("読み込み失敗: " + e.getMessage());
        }
    }
}