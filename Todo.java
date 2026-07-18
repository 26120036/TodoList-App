package practice.Todo;

public class Todo {
    private final String title;
    private boolean completed;

    // コンストラクタ
    public Todo(String title) {
        this.title = title;
        this.completed = false;
    }

    // 外部からの取得用
    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    // 完了状態にする
    public void complete() {
        this.completed = true;
    }
}