package byc.avt.avanteelender.helper;

import junit.framework.TestCase;

public class FungsiTest extends TestCase {

    public void testToNumb() {
        int in = 199964;
        String out;
        String exp = "Rp199.964,00";
        //double delta = .1;

        Fungsi f = new Fungsi();
        out = f.toNumb(""+in);
        assertEquals(exp, out);
    }
}