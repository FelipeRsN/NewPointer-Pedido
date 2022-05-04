package newpointer.com.br.newpointerpedido.CustomAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import newpointer.com.br.newpointerpedido.Activity.MainActivity;
import newpointer.com.br.newpointerpedido.Activity.MinhaContaActivity;
import newpointer.com.br.newpointerpedido.Activity.listeners.PergComandaListener;
import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection;
import newpointer.com.br.newpointerpedido.R;

/**
 * Created by FelipeRsN on 7/4/16.
 */
public class PergComandaCustomDialog extends Dialog implements View.OnClickListener {
    private Activity act;
    private Context ctx;
    private PergComandaListener listener;
    private Button bt_0;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Button bt_6;
    private Button bt_7;
    private Button bt_8;
    private Button bt_9;
    private ImageButton bt_back;
    private ImageButton bt_scan;
    private ImageButton bt_ok;
    private TextView mesa_cod;
    private String stringcodigo = "";
    private Button close;
    private String comanda;
    private TextView cd_comanda;
    private TextView comandaLabel;
    private DBLiteConnection dbl;
    private boolean openMinhaConta = false;

    public PergComandaCustomDialog(Context context, Activity act, String mesa, Boolean openMinhaConta, String comanda, PergComandaListener listener) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.listener = listener;
        this.comanda = mesa;
        this.stringcodigo = comanda;
        this.openMinhaConta = openMinhaConta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_perguntamesa);
        startVar();
    }

    private void startVar() {
        dbl = new DBLiteConnection(act);
        bt_0 = (Button) findViewById(R.id.bt_pergmesa_0);
        bt_1 = (Button) findViewById(R.id.bt_pergmesa_1);
        bt_2 = (Button) findViewById(R.id.bt_pergmesa_2);
        bt_3 = (Button) findViewById(R.id.bt_pergmesa_3);
        bt_4 = (Button) findViewById(R.id.bt_pergmesa_4);
        bt_5 = (Button) findViewById(R.id.bt_pergmesa_5);
        bt_6 = (Button) findViewById(R.id.bt_pergmesa_6);
        bt_7 = (Button) findViewById(R.id.bt_pergmesa_7);
        bt_8 = (Button) findViewById(R.id.bt_pergmesa_8);
        bt_9 = (Button) findViewById(R.id.bt_pergmesa_9);
        comandaLabel = (TextView) findViewById(R.id.tv_pergmesa_noperador);
        bt_back = (ImageButton) findViewById(R.id.ib_pergmesa_backspace);
        bt_ok = (ImageButton) findViewById(R.id.ib_pergmesa_ok);
        bt_scan = (ImageButton) findViewById(R.id.bt_scan);
        mesa_cod = (TextView) findViewById(R.id.tv_pergmesa_codigo);
        close = (Button) findViewById(R.id.bt_pergmesa_close);
        cd_comanda = (TextView) findViewById(R.id.tv_pergmesa_comanda);

        comandaLabel.setText(dbl.selectConfig().getTitulo_loja());

        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        close.setOnClickListener(this);
        bt_scan.setOnClickListener(this);

        cd_comanda.setText("MESA: " + this.comanda);

        if (!this.stringcodigo.isEmpty()) {
            mesa_cod.setText(this.stringcodigo);
        }else{
            mesa_cod.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bt_ok) {
            Intent i = new Intent();
            if (mesa_cod.getText().toString().length() > 0) {

                String stringmesa = String.format("%6s", mesa_cod.getText().toString()).replace(' ', '0');

                if (dbl.selectConfig().getDigito_verificador() == 1) {
                    int impar = ((Integer.parseInt(stringmesa.substring(4, 5))) + (Integer.parseInt(stringmesa.substring(2, 3))) + (Integer.parseInt(stringmesa.substring(0, 1)))) * 3;
                    int par = (Integer.parseInt(stringmesa.substring(3, 4))) + (Integer.parseInt(stringmesa.substring(1, 2)));
                    int soma = par + impar;
                    String so = "" + soma;
                    int tam = so.length();
                    int digito = Integer.parseInt(so.substring(tam - 1));
                    if (digito != 0) {
                        digito = digito - 10;
                    }
                    digito = digito * (-1);
                    if (digito != (Integer.parseInt(stringmesa.substring(5, 6)))) {
                        Toast.makeText(act, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                        mesa_cod.setText("");
                    } else {

                        if (openMinhaConta) {
                            i.setClass(act, MinhaContaActivity.class);
                            i.putExtra("numeroMesa", stringmesa);
                            i.putExtra("gerador", dbl.selectConfig().getTitulo_loja());
                            act.startActivity(i);
                            dismiss();
                        } else {
                            i.setClass(act, MainActivity.class);
                            i.putExtra("numeroMesa", comanda);
                            i.putExtra("mesa", stringmesa);
                            act.startActivity(i);
                            dismiss();
                        }

                    }
                } else {
                    if (openMinhaConta) {
                        i.setClass(act, MinhaContaActivity.class);
                        i.putExtra("numeroMesa", stringmesa);
                        i.putExtra("gerador", dbl.selectConfig().getTitulo_loja());
                        act.startActivity(i);
                        dismiss();
                    } else {
                        i.setClass(act, MainActivity.class);
                        i.putExtra("numeroMesa", comanda);
                        i.putExtra("mesa", stringmesa);
                        act.startActivity(i);
                        dismiss();
                    }
                }
            } else {
                Toast.makeText(act, "Digito obrigatório", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == bt_0) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "0";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_1) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "1";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_2) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "2";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_3) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "3";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_4) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "4";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_5) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "5";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_6) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "6";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_7) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "7";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_8) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "8";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_9) {
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo + "9";
            mesa_cod.setText(stringcodigo);
        }
        if (view == bt_back) {
            stringcodigo = mesa_cod.getText().toString();
            if (!stringcodigo.isEmpty())
                stringcodigo = stringcodigo.substring(0, stringcodigo.length() - 1);
            mesa_cod.setText(stringcodigo);
        }
        if (view == close) {
            dismiss();
        }
        if (view == bt_scan) {
            listener.onOpenScanClicked();
            dismiss();
        }
    }
}
