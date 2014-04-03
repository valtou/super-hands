
package spaceavoider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author valtou
 */

public class TulosManageri {

    private ArrayList<Tulokset> tulokset;

    private static final String TULOSTIEDOSTO = "tulokset.dat";

    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public TulosManageri() {

        tulokset = new ArrayList<Tulokset>();
    }

    public ArrayList<Tulokset> getTulos() {

        lataaTulosTiedosto();
        sort();
        return tulokset;

    }

    private void sort() {
        TulosVertailu vertailu = new TulosVertailu();
        Collections.sort(tulokset, vertailu);
    }

    public void addTulos(String nimi, int tulos) {
        lataaTulosTiedosto();
        tulokset.add(new Tulokset(nimi, tulos));
        paivitaTulosKentta();
    }

    public void lataaTulosTiedosto() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(TULOSTIEDOSTO));
            tulokset = (ArrayList<Tulokset>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Tiedostoa ei löydy!");
        } catch (IOException e) {
            System.out.println("Tiedonsiirrossa vikaa!");
        } catch (ClassNotFoundException e) {
            System.out.println("Luokkaa ei löydy!");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("IO-poikkeus!");
            }
        }
    }

    public void paivitaTulosKentta() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(TULOSTIEDOSTO));
            outputStream.writeObject(tulokset);
        } catch (FileNotFoundException e) {
            System.out.println("Tiedostoa ei löydy!");
        } catch (IOException e) {
            System.out.println("Tiedonsiirrossa vikaa!");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Päivitysvirhe!");
            }
        }
    }

    public String getHuipputulosString() {
        String huipputulosString = "";
        final int MAX = 10;

        ArrayList<Tulokset> tulokset;
        tulokset = getTulos();

        int i = 0;
        int x = tulokset.size();
        if (x > MAX) {
            x = MAX;
        }
        while (i < x) {
            huipputulosString += (i + 1) + ". " + " " + tulokset.get(i).getNimi() + "\t" + tulokset.get(i).getTulos() + "\n";
            i++;
        }

        return huipputulosString;

    }

}
