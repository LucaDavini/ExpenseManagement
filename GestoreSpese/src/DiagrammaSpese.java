
import javafx.scene.chart.*;

public class DiagrammaSpese extends LineChart {
    
    public DiagrammaSpese(Axis asseX, Axis asseY) {
        super(asseX, asseY);
        
        setLegendVisible(false);    // 1)
        getXAxis().setTickLabelsVisible(false);
        getXAxis().setTickMarkVisible(false);
        
        
        setPrefHeight(300);
    }
    
    public void popolaDiagramma (double[] speseGiorni) {
        getData().clear();
        
        XYChart.Series listaSpese = new XYChart.Series();
        
        for (int i = 0; i < speseGiorni.length; i++) {
            listaSpese.getData().add(new XYChart.Data(i, speseGiorni[i]));
        }

        getData().addAll(new XYChart.Series(), new XYChart.Series(), new XYChart.Series(), listaSpese);
    }
    
    public void aggiungiSpesa (int diffGiorni, double spesa) {
        XYChart.Series listaSpese = (XYChart.Series) getData().get(3);
        int index = (listaSpese.getData().size() - 1) - diffGiorni;
        double spesaAttuale = (double) ((XYChart.Data)listaSpese.getData().get(index)).getYValue();
        
        listaSpese.getData().set(index, new XYChart.Data(index, spesaAttuale + spesa));     // 2)
    }
}


// 1) elimino la visibilità di alcuni elementi per avere un grafico più pulito

// 2) modifico l'elemento del diagramma corrispondente alla data della nuova spesa