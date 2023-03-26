import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static GraphicsContext g;

    public static final long TIMESTEP = 30;
    public static int w = 1920, h = 1080;
//    public static int w = 800, h = 800;
    public static double x, y;
    public static boolean clicking = false;
    public Grid grid;

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(bp);
        Canvas canvas = new Canvas(w, h);
        bp.setCenter(canvas);
        g = canvas.getGraphicsContext2D();
        grid = new Grid(w, h);

//        scene.setCursor(Cursor.NONE);

        scene.setRoot(bp);
        stage.setScene(scene);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
                    case R:
                        g.clearRect(0, 0, w, h);
                        grid = new Grid(w, h);
                        break;
                    case O:
                        break;
                    case P:
                        break;
                    case X:
                        System.exit(0);
                        break;
                }
            }
        });
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clicking = true;
                x = event.getX();
                y = event.getY();
            }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clicking = true;
                x = event.getX();
                y = event.getY();
            }
        });
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clicking = false;
            }
        });
        stage.setFullScreen(true);
        stage.show();

        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::tick, 0, TIMESTEP, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        CountDownLatch drawLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            g.clearRect(0, 0, w, h);
            grid.draw(g);
            drawLatch.countDown();
        });
        grid.step();
        try {
            drawLatch.await();
        } catch (InterruptedException ignore) {}
    }

    @Override
    public void stop() throws Exception{
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
