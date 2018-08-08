package com.cyl.court;

import com.cyl.court.view.MainView;
import com.cyl.court.view.ViewDispatcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    final PageData[] pages = new PageData[] {
            new PageData(
                    "The apple is the pomaceous fruit of the apple tree, species Malus "
                            + "domestica in the rose family (Rosaceae). It is one of the most "
                            + "widely cultivated tree fruits, and the most widely known of "
                            + "the many members of genus Malus that are used by humans. "
                            + "The tree originated in Western Asia, where its wild ancestor, "
                            + "the Alma, is still found today.",
                    "Malus domestica"),
            new PageData(
                    "The hawthorn is a large genus of shrubs and trees in the rose "
                            + "family, Rosaceae, native to temperate regions of the Northern "
                            + "Hemisphere in Europe, Asia and North America. "
                            + "The name hawthorn was "
                            + "originally applied to the species native to northern Europe, "
                            + "especially the Common Hawthorn C. monogyna, and the unmodified "
                            + "name is often so used in Britain and Ireland.",
                    "Crataegus monogyna"),
            new PageData(
                    "The ivy is a flowering plant in the grape family (Vitaceae) native to"
                            + " eastern Asia in Japan, Korea, and northern and eastern China. "
                            + "It is a deciduous woody vine growing to 30 m tall or more given "
                            + "suitable support,  attaching itself by means of numerous small "
                            + "branched tendrils tipped with sticky disks.",
                    "Parthenocissus tricuspidata"),
            new PageData(
                    "The quince is the sole member of the genus Cydonia and is native to "
                            + "warm-temperate southwest Asia in the Caucasus region. The "
                            + "immature fruit is green with dense grey-white pubescence, most "
                            + "of which rubs off before maturity in late autumn when the fruit "
                            + "changes color to yellow with hard, strongly perfumed flesh.",
                    "Cydonia oblonga")
    };

    final String[] viewOptions = new String[] {
            "Title",
            "Binomial name",
            "Picture",
            "Description"
    };

    final Entry<String, Effect>[] effects = new Entry[] {
            new SimpleEntry<>("Sepia Tone", new SepiaTone()),
            new SimpleEntry<>("Glow", new Glow()),
            new SimpleEntry<>("Shadow", new DropShadow())
    };

    final ImageView pic = new ImageView();
    final Label name = new Label();
    final Label binName = new Label();
    final Label description = new Label();


    private MainView mainView = new MainView();

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
//        stage.setTitle("Menu Sample");

//        ViewDispatcher.open(FieldMapView.class, Modality.WINDOW_MODAL);
        ViewDispatcher.open(MainView.class, Modality.WINDOW_MODAL);
//        ViewDispatcher.open(TreeStructView.class, Modality.WINDOW_MODAL);

//        stage.show();
    }


    private class PageData {
        public String name;
        public String description;
        public String binNames;
        public Image image;
        public PageData( String description, String binNames) {
//            this.name = name;
            this.description = description;
            this.binNames = binNames;
//            image = new Image(getClass().getResourceAsStream(name + ".jpg"));
        }
    }
}