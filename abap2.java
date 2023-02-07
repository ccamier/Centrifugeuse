import com.cycling74.max.*;
import com.cycling74.msp.*;

public class abap2 extends MSPPerformer
{
	private static final double _dpi=6.2831853071796f;
	private int _on = 2;  // 0: off   1: stopping 2: on
    private int _running = 1;
	private int _nspk = 1;
	private double _an;
	private double _sinan;
	private double _anghp1;
	private double _p = 2.85;

	private static final String[] INLET_ASSIST = new String[]{
		"input (sig)", "input angle [0..1] (sig)"
    };
	private static final String[] OUTLET_ASSIST = new String[]{
		"speaker output (sig)"
	};
	

	public abap2(int nspk, double anghp1)
	{
		int i;
		int[] outs;

		declareInlets(new int[]{SIGNAL,SIGNAL});
		outs = new int[nspk];
        for (i=0; i<nspk; i++) { outs[i]=SIGNAL; }; 
		declareOutlets(outs);

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

		_nspk = nspk;
		_an = _dpi/(double)_nspk;
		_sinan = Math.sin(_an);
		_anghp1 = anghp1;

	}
    

	public void inlet(int p)
	{
        if (p!=0) {
			_on=2;
		} 
		else {
			_on=1;
		}
	}

	public void dspsetup(MSPSignal[] ins, MSPSignal[] outs)
	{


	}

	private double sigmo(double alpha, double param)
	{
		double inter =-param*(alpha/_an-0.5); 
		double result = 1./(1 + Math.exp(inter));
		return result;
	} 

	public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		int t,i;
		
		if (_on != 2) {
		  if (_on == 1) { 
			for(t = 0; t < ins[0].vec.length;t++)
				for (i=0; i<_nspk; i++) {
					outs[i].vec[t] = 0.0f;
				}
			_on = 0;
          }
          return;
		}

		int spk;
		double a, g1, g2, m;
		float[] in = ins[0].vec;
		double angle;


		for(t = 0; t < in.length;t++)
		{
			for (i=0; i<_nspk; i++) {
				outs[i].vec[t] = 0.0f;
			}
			angle = ins[1].vec[t] % _dpi - _anghp1;
			if (angle <0) { angle = angle +_dpi; }

			spk = (int)Math.floor(angle/_dpi*_nspk);
			a = angle-_an*spk;

//			g2 = ( sigmo(a,_p) - sigmo(0,_p) ) / ( sigmo(1.,_p) - sigmo(0.,_p));
			g2 = ( sigmo(a,_p) - 0.19387893782386 ) / 0.61224212435228 ;

			g1 = 1-g2;



			m = Math.sqrt(g1*g1+g2*g2);


			outs[spk].vec[t] = (float)(g1/m*in[t]);

			spk = spk +1;
			if (spk > _nspk-1) { spk = 0; }
			outs[spk].vec[t] = (float)(g2/m*in[t]);

		}
	}
}








