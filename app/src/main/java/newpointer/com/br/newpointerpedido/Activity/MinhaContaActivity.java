package newpointer.com.br.newpointerpedido.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Properties;

import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection;
import newpointer.com.br.newpointerpedido.Model.ListaProdModel;
import newpointer.com.br.newpointerpedido.R;

public class MinhaContaActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton back;
    private TextView conta;
    private Intent j;
    private TextView aviso;
    private TableLayout tl;
    private String message;
    private String mesa;
    private DBLiteConnection dbl;
    private ProgressBar pb;
    private TextView tv_conta_prod;
    private TextView tv_conta_tax;
    private TextView tv_conta_tot;
    private ArrayList<ListaProdModel> prod = new ArrayList<ListaProdModel>();
    private ArrayList<Long> cdpro = new ArrayList<Long>();
    private ArrayList<String> dspro = new ArrayList<String>();
    private ArrayList<String> qtdpro = new ArrayList<String>();
    private ArrayList<Double> vlpro = new ArrayList<Double>();
    private ArrayList<Double> totpro = new ArrayList<Double>();

    private Double totalTaxa = 0.0;
    private Double totalGeral = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        startVar();

        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void startVar() {
        back = (ImageButton) findViewById(R.id.ib_conta_return);
        conta = (TextView) findViewById(R.id.tv_conta_nmesa);
        tl = (TableLayout) findViewById(R.id.tl_conta);
        pb = (ProgressBar) findViewById(R.id.pb_minhaconta);
        aviso = (TextView) findViewById(R.id.tv_minhaconta_aviso);
        tv_conta_prod = (TextView) findViewById(R.id.tv_conta_tprod);
        tv_conta_tax = (TextView) findViewById(R.id.tv_conta_tax);
        tv_conta_tot = (TextView) findViewById(R.id.tv_conta_tot);
        dbl = new DBLiteConnection(MinhaContaActivity.this);


        back.setOnClickListener(this);

        j = getIntent();
        mesa = j.getStringExtra("numeroMesa");
        conta.setText(j.getStringExtra("gerador")+" "+mesa);
    }

    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
        }
    }

    public class GetProduct extends AsyncTask<String,Object,Integer> {
        private int id_conta = 0;
        private boolean haveConta = false;
        private double total = 0.0;
        @Override
        protected Integer doInBackground(String... bt) {
            try{
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
            try{
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");
                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://"+dbl.selectConfig().getString_bd()+"", props);
                String sSql = "Select * from minha_conta_android('"+dbl.selectConfig().getTitulo_loja()+"','"+dbl.selectConfig().getEstacao()+"','"+mesa+"')";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while (rs.next()){
                    if(rs.getString("RETORNO").equalsIgnoreCase("OK")){
                        haveConta = true;
                        Log.i("Retorno",rs.getString("RETORNO"));
                        Log.i("CD_PRODUTO",rs.getString("CD_PRODUTO"));
                        Log.i("DS_PRO",rs.getString("DS_PRO"));
                        Log.i("QT_CONSUMO",rs.getString("QT_CONSUMO"));
                        Log.i("VL_CONSUMO",rs.getString("VL_CONSUMO"));
                        Log.i("TP_ESTACAO",rs.getString("TP_ESTACAO"));
                        Log.i("VL_INDICE",rs.getString("VL_INDICE"));
                        Double val_prod = 0.0;

                        if(rs.getString("VL_CONSUMO") != null){
                            val_prod = Double.parseDouble(rs.getString("VL_CONSUMO"));
                        }else{
                            val_prod = 0.0;
                        }
                        total = total + val_prod;

                        try {
                            if (Integer.parseInt(rs.getString("TP_ESTACAO")) > 0) {
                                totalTaxa = totalTaxa + (Double.parseDouble(rs.getString("VL_CONSUMO")) * Double.parseDouble(rs.getString("VL_INDICE")));
                            }
                        }catch (Exception e){
                            totalTaxa = total;
                        }

                        Log.i("INDICE", "doInBackground: "+rs.getString("VL_INDICE")+" - "+rs.getString("TP_ESTACAO"));
                        prod.add(new ListaProdModel(Long.parseLong(rs.getString("CD_PRODUTO")),rs.getString("DS_PRO"),rs.getString("QT_CONSUMO"), rs.getString("VL_CONSUMO"),Double.parseDouble(rs.getString("VL_CONSUMO")),Integer.parseInt(rs.getString("TP_ESTACAO")), Double.parseDouble(rs.getString("VL_INDICE"))));
                    }else{
                        message = rs.getString("RETORNO");
                    }

                }
//                String sSql = "Select * from BUSCA_CONTA_ABERTA_PDV ('"+mesa+"','F','X')";
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery(sSql);
//                while (rs.next()){
//                    Log.i("idconta", rs.getInt("ID_CONTA")+"");
//                    id_conta = rs.getInt("ID_CONTA");
//                    haveConta = true;
//                }
//                rs.close();
//                if(haveConta){
//                    String sSql2 = "Select * from Busca_Produto_Pdv("+id_conta+")";
//                    ResultSet rs2 = stmt.executeQuery(sSql2);
//                    while (rs2.next()){
//                        cdpro.add(Long.parseLong(rs2.getString("CD_PRODUTO")));
//                        dspro.add(rs2.getString("DS_PRO"));
//                        qtdpro.add(rs2.getString("QT_CONSUMO"));
//                    }
//                    rs2.close();
//
//                    int cont2 = 0;
//                    while(cont2<cdpro.size()){
//                        String stringCod = String.format("%14s", cdpro.get(cont2)+"").replace(' ', '0');
//                        ResultSet rs3 = stmt.executeQuery("SELECT VL_VENDA_PRO FROM PRODUTO WHERE CD_PRO = '"+stringCod+"'");
//                        if(rs3.next()){
//                            total = total + Double.parseDouble(rs3.getString("VL_VENDA_PRO"))*Double.parseDouble(qtdpro.get(cont2));
//                            prod.add(new ListaProdModel(cdpro.get(cont2),dspro.get(cont2),qtdpro.get(cont2), rs3.getString("VL_VENDA_PRO"),Double.parseDouble(rs3.getString("VL_VENDA_PRO"))*Double.parseDouble(qtdpro.get(cont2))));
//                        }
//                        cont2++;
//                    }
//                }
                conn.close();
                stmt.close();
                return 1;
            }
            catch(SQLException e1){
                e1.printStackTrace();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            if(i == 1){
                if(haveConta){
                    int cont = 0;
                    while (cont < prod.size()){
                        TableRow tr = new TableRow(MinhaContaActivity.this);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tr.setWeightSum(5);

                        TextView tv_name = new TextView(MinhaContaActivity.this);
                        TextView tv_qtd = new TextView(MinhaContaActivity.this);
                        TextView tv_tot = new TextView(MinhaContaActivity.this);

                        String prod_name;
                        if(prod.get(cont).getName().length() > 25) prod_name = prod.get(cont).getName().substring(0, 25);
                        else prod_name = prod.get(cont).getName();
                        tv_name.setText(prod_name);
                        tv_qtd.setText(prod.get(cont).getQuant()+"");
                        NumberFormat formatarFloat = new DecimalFormat("0.00");
                        tv_tot.setText("R$ "+formatarFloat.format(prod.get(cont).getTot()));

                        tv_qtd.setGravity(Gravity.CENTER);
                        tv_tot.setGravity(Gravity.RIGHT);
                        tv_tot.setPadding(0,0,3,0);
                        tv_name.setPadding(3,0,0,0);

                        tv_name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f));
                        tv_qtd.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                        tv_tot.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                        tr.addView(tv_name);
                        tr.addView(tv_qtd);
                        tr.addView(tv_tot);
                        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        cont++;
                    }
                    NumberFormat formatarFloat = new DecimalFormat("0.00");
                    pb.setVisibility(View.INVISIBLE);
                    tv_conta_prod.setText("R$ "+formatarFloat.format(total));
                    tv_conta_tax.setText("R$ "+formatarFloat.format(totalTaxa));
                    tv_conta_tot.setText("R$ "+formatarFloat.format(total+totalTaxa));

                }else{
                    pb.setVisibility(View.INVISIBLE);
                    aviso.setText(message);
                    aviso.setVisibility(View.VISIBLE);
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MinhaContaActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Falha ao conectar-se");
                builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
                builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pb.setVisibility(View.VISIBLE);
                        GetProduct gp = new GetProduct();
                        gp.execute();
                    }
                });
                builder.setNegativeButton("Fechar", null);
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
}
