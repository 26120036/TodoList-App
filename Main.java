package practice.Todo;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final TodoManager todoManager = new TodoManager();

    public static void main(String[] args) {
        // シャットダウンフック
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nプログラムを終了します。データを保存中...");
            todoManager.save();
        }));

        todoManager.load();

        while (true) {
            showMenu();
            String input = prompt("番号: ");

            int command;
            try {
                command = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("数字以外の入力を検知しました。0〜6の数字で入力してください。");
                continue;
            }

            if (command == 0) {
                System.out.println("終了します。");
                break;
            }

            executeCommand(command);
        }
    }

    private static void executeCommand(int command) {
        switch (command) {
            case 1:
                addTask();
                break;
            case 2:
                todoManager.show();
                break;
            case 3:
                completeTask();
                break;
            case 4:
                deleteTask();
                break;
            case 5:
                todoManager.save();
                break;
            case 6: 
                todoManager.load();
                break;
            default:
                System.out.println("0~6で入力してください");
                break;
        }
    }

    private static void addTask() {
        String title = prompt("タスク名を入力してください: ");
        // スペースだけのタスクが作られないようにisBlank()で検査
        if (title.isBlank()) {
            System.out.println("タスク名は空にできません。");
            return;
        }
        todoManager.add(new Todo(title)); 
    }

    private static void completeTask() {
        todoManager.show();
        try {
            int task_number = Integer.parseInt(prompt("完了するタスクの番号: "));
            todoManager.complete(task_number);
        } catch (NumberFormatException e) {
            System.out.println("数字を入力してください。");
        }
    }

    private static void deleteTask() {
        todoManager.show();
        try {
            int task_number = Integer.parseInt(prompt("削除するタスクの番号: "));
            todoManager.remove(task_number);
        } catch (NumberFormatException e) {
            System.out.println("数字を入力してください。");
        }
    }

    // 入力を受け取るためのメソッド
    private static String prompt(String message) {
        System.out.print(message);
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private static void showMenu() {
        System.out.println("\n========== TODO管理 ==========");
        System.out.println("1. 追加 | 2. 一覧 | 3. 完了 | 4. 削除 | 5. 保存 | 6. 読込 | 0. 終了");
    }
}