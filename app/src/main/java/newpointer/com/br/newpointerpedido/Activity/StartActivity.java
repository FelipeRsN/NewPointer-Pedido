package newpointer.com.br.newpointerpedido.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection;
import newpointer.com.br.newpointerpedido.CustomAdapter.OperadorCustomDialog;
import newpointer.com.br.newpointerpedido.CustomAdapter.PergComandaCustomDialog;
import newpointer.com.br.newpointerpedido.Model.ConfigModel;
import newpointer.com.br.newpointerpedido.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
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
    private Button configuration;
    private Button my_account;
    private Button make_order;
    private TextView comanda;
    private TextView titulo;
    private String nmesa = "";
    private DBLiteConnection dbl;
    private ConfigModel config;
    private Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startVar();

        if (config.getOperacao_selection() == 1) {
            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) (100 * scale + 0.5f);
            bt_0.getLayoutParams().width = pixels;
            bt_0.getLayoutParams().height = pixels;
            bt_1.getLayoutParams().width = pixels;
            bt_1.getLayoutParams().height = pixels;
            bt_2.getLayoutParams().width = pixels;
            bt_2.getLayoutParams().height = pixels;
            bt_3.getLayoutParams().width = pixels;
            bt_3.getLayoutParams().height = pixels;
            bt_4.getLayoutParams().width = pixels;
            bt_4.getLayoutParams().height = pixels;
            bt_5.getLayoutParams().width = pixels;
            bt_5.getLayoutParams().height = pixels;
            bt_6.getLayoutParams().width = pixels;
            bt_6.getLayoutParams().height = pixels;
            bt_7.getLayoutParams().width = pixels;
            bt_7.getLayoutParams().height = pixels;
            bt_8.getLayoutParams().width = pixels;
            bt_8.getLayoutParams().height = pixels;
            bt_9.getLayoutParams().width = pixels;
            bt_9.getLayoutParams().height = pixels;
            int font = (int) (40 * scale + 0.5f);
            bt_0.setTextSize(font);
            bt_1.setTextSize(font);
            bt_2.setTextSize(font);
            bt_3.setTextSize(font);
            bt_4.setTextSize(font);
            bt_5.setTextSize(font);
            bt_6.setTextSize(font);
            bt_7.setTextSize(font);
            bt_8.setTextSize(font);
            bt_9.setTextSize(font);
        }

    }

    private void startVar() {
        bt_0 = (Button) findViewById(R.id.bt_start_0);
        bt_1 = (Button) findViewById(R.id.bt_start_1);
        bt_2 = (Button) findViewById(R.id.bt_start_2);
        bt_3 = (Button) findViewById(R.id.bt_start_3);
        bt_4 = (Button) findViewById(R.id.bt_start_4);
        bt_5 = (Button) findViewById(R.id.bt_start_5);
        bt_6 = (Button) findViewById(R.id.bt_start_6);
        bt_7 = (Button) findViewById(R.id.bt_start_7);
        bt_8 = (Button) findViewById(R.id.bt_start_8);
        bt_9 = (Button) findViewById(R.id.bt_start_9);
        bt_back = (ImageButton) findViewById(R.id.ib_start_backspace);
        my_account = (Button) findViewById(R.id.bt_start_conta);
        make_order = (Button) findViewById(R.id.bt_start_pedido);
        configuration = (Button) findViewById(R.id.bt_start_config);
        comanda = (TextView) findViewById(R.id.tv_start_comanda);
        titulo = (TextView) findViewById(R.id.tv_start_tmesa);
        dbl = new DBLiteConnection(StartActivity.this);
        i = new Intent();

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
        my_account.setOnClickListener(this);
        make_order.setOnClickListener(this);
        configuration.setOnClickListener(this);

        config = dbl.selectConfig();
        if (config.getPergunta_mesa() == 1) {
            titulo.setText("MESA");
        } else {
            titulo.setText(config.getTitulo_loja());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bt_0) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "0";
            comanda.setText(nmesa);
        }
        if (view == bt_1) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "1";
            comanda.setText(nmesa);
        }
        if (view == bt_2) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "2";
            comanda.setText(nmesa);
        }
        if (view == bt_3) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "3";
            comanda.setText(nmesa);
        }
        if (view == bt_4) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "4";
            comanda.setText(nmesa);
        }
        if (view == bt_5) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "5";
            comanda.setText(nmesa);
        }
        if (view == bt_6) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "6";
            comanda.setText(nmesa);
        }
        if (view == bt_7) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "7";
            comanda.setText(nmesa);
        }
        if (view == bt_8) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "8";
            comanda.setText(nmesa);
        }
        if (view == bt_9) {
            nmesa = comanda.getText().toString();
            nmesa = nmesa + "9";
            comanda.setText(nmesa);
        }
        if (view == bt_back) {
            nmesa = comanda.getText().toString();
            if (!nmesa.isEmpty()) nmesa = nmesa.substring(0, nmesa.length() - 1);
            comanda.setText(nmesa);
        }
        if (view == make_order) {
            if (comanda.getText().toString().isEmpty()) {
                Toast.makeText(StartActivity.this, "Digito da mesa obrigatório", Toast.LENGTH_SHORT).show();
            } else {
                int ncomanda = Integer.parseInt(comanda.getText().toString());
                if (ncomanda < Integer.parseInt(config.getNmin_mesa()) || ncomanda > Integer.parseInt(config.getNmax_mesa())) {
                    Toast.makeText(StartActivity.this, "Numero da mesa fora do limite", Toast.LENGTH_SHORT).show();
                    comanda.setText("");
                    nmesa = "";
                } else {

                    String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');

                    if (config.getPergunta_mesa() == 1) {

                        PergComandaCustomDialog pmcd = new PergComandaCustomDialog(StartActivity.this, StartActivity.this, stringComanda, false);
                        pmcd.setCancelable(false);
                        pmcd.setCanceledOnTouchOutside(false);
                        pmcd.show();

                        comanda.setText("");

                    } else {

                        if (dbl.selectConfig().getDigito_verificador() == 1) {
                            int impar = ((Integer.parseInt(stringComanda.substring(4, 5))) + (Integer.parseInt(stringComanda.substring(2, 3))) + (Integer.parseInt(stringComanda.substring(0, 1)))) * 3;
                            int par = (Integer.parseInt(stringComanda.substring(3, 4))) + (Integer.parseInt(stringComanda.substring(1, 2)));
                            int soma = par + impar;
                            String so = "" + soma;
                            int tam = so.length();
                            int digito = Integer.parseInt(so.substring(tam - 1));
                            if (digito != 0) {
                                digito = digito - 10;
                            }
                            digito = digito * (-1);
                            if (digito != (Integer.parseInt(stringComanda.substring(5, 6)))) {
                                Toast.makeText(StartActivity.this, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                                comanda.setText("");
                                nmesa = "";
                            } else {
                                comanda.setText("");
                                nmesa = "";

                                i.setClass(StartActivity.this, MainActivity.class);
                                i.putExtra("numeroMesa", stringComanda);
                                startActivity(i);
                            }
                        } else {
                            comanda.setText("");
                            nmesa = "";

                            i.setClass(StartActivity.this, MainActivity.class);
                            i.putExtra("numeroMesa", stringComanda);
                            startActivity(i);
                        }

                    }
                }
            }
        }
        if (view == my_account) {
            if (comanda.getText().toString().isEmpty()) {
                Toast.makeText(StartActivity.this, "Digito da mesa obrigatório", Toast.LENGTH_SHORT).show();
            } else {
                int ncomanda = Integer.parseInt(comanda.getText().toString());
                if (ncomanda < Integer.parseInt(config.getNmin_mesa()) || ncomanda > Integer.parseInt(config.getNmax_mesa())) {
                    Toast.makeText(StartActivity.this, "Numero da mesa fora do limite", Toast.LENGTH_SHORT).show();
                } else {

                    String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');

                    if (config.getPergunta_mesa() == 1) {

                        PergComandaCustomDialog pmcd = new PergComandaCustomDialog(StartActivity.this, StartActivity.this, stringComanda, true);
                        pmcd.setCancelable(false);
                        pmcd.setCanceledOnTouchOutside(false);
                        pmcd.show();

                        comanda.setText("");

                    } else {

                        if (dbl.selectConfig().getDigito_verificador() == 1) {
                            int impar = ((Integer.parseInt(stringComanda.substring(4, 5))) + (Integer.parseInt(stringComanda.substring(2, 3))) + (Integer.parseInt(stringComanda.substring(0, 1)))) * 3;
                            int par = (Integer.parseInt(stringComanda.substring(3, 4))) + (Integer.parseInt(stringComanda.substring(1, 2)));
                            int soma = par + impar;
                            String so = "" + soma;
                            int tam = so.length();
                            int digito = Integer.parseInt(so.substring(tam - 1));
                            if (digito != 0) {
                                digito = digito - 10;
                            }
                            digito = digito * (-1);
                            if (digito != (Integer.parseInt(stringComanda.substring(5, 6)))) {
                                Toast.makeText(StartActivity.this, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                                comanda.setText("");
                                nmesa = "";
                            } else {
                                comanda.setText("");
                                nmesa = "";

                                i.setClass(StartActivity.this, MinhaContaActivity.class);
                                i.putExtra("numeroMesa", stringComanda);
                                i.putExtra("gerador", dbl.selectConfig().getTitulo_loja());
                                startActivity(i);
                            }
                        } else {
                            comanda.setText("");
                            nmesa = "";

                            i.setClass(StartActivity.this, MinhaContaActivity.class);
                            i.putExtra("numeroMesa", stringComanda);
                            i.putExtra("gerador", dbl.selectConfig().getTitulo_loja());
                            startActivity(i);
                        }

                    }
                }
            }
        }
        if (view == configuration) {
            OperadorCustomDialog ocd = new OperadorCustomDialog(StartActivity.this, StartActivity.this);
            ocd.setCanceledOnTouchOutside(false);
            ocd.setCancelable(false);
            ocd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            ocd.show();
        }


    }
}
