import com.cycling74.max.*;
import com.cycling74.msp.*;

public class fdelay extends MSPPerformer
{
	private int _buffersize;
	private float _maxdelay;
	private float[] _buffer;
	private int _cur;

	private static final String[] INLET_ASSIST = new String[]{
		"input (sig)", "delay (s) (sig)"
	};
	private static final String[] OUTLET_ASSIST = new String[]{
		"output (sig)"
	};
	

	public fdelay(float maxdelay)
	{
		declareInlets(new int[]{SIGNAL,SIGNAL});
		declareOutlets(new int[]{SIGNAL});

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

	    _buffersize=0;
		_maxdelay = maxdelay;
	}
    
	public void dspsetup(MSPSignal[] ins, MSPSignal[] outs)
	{
		_buffersize=(int)Math.ceil(ins[0].sr * _maxdelay) + 3; // 3 extra samples for lagrange interp
		_buffer = new float[_buffersize];
		_cur = 0;
	}

	private double[] lagrange5(double d)
	{
		double[] coefs = { (d-1)*(d-2)*(d-3)*(d-4)/24.0,
				 -d*(d-2)*(d-3)*(d-4)/6.0,
				 d*(d-1)*(d-3)*(d-4)/4.0,
				 -d*(d-1)*(d-2)*(d-4)/6.0,
				 d*(d-1)*(d-2)*(d-3)/24.0 };

		return coefs;
				 
	}

	public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		int i,l,t;
		double d;
		int d_int;
		double d_lagrange;
		double[] coefs;
		double v;

		for (i = 0; i < ins[0].vec.length; i++)
		{
			_buffer[_cur] = ins[0].vec[i];

			d = ins[1].vec[i] * ins[0].sr;

			// put a warning
			if (d<2.0) { d = 2.0; }
			if (d>_buffersize-3) { d = _buffersize-3; } 

			d_int = (int)Math.round(d)-2;
			d_lagrange = d - d_int;
			coefs = lagrange5(d_lagrange);

			t = _cur - d_int;
			if (t<0) { t = t + _buffersize; }
			v = 0.0;
			for (l=0; l<5; l++) {
			  v = v + coefs[l] * _buffer[t];
			  t = t-1;
			  if (t<0) { t = t + _buffersize; }
			}
			outs[0].vec[i] = (float)v;



			_cur++;
			if (_cur >= _buffersize) _cur=0;			

		}
	}
}




