package newpointer.com.br.newpointerpedido.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.readystatesoftware.viewbadger.BadgeView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import newpointer.com.br.newpointerpedido.Activity.listeners.PergComandaListener;
import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection;
import newpointer.com.br.newpointerpedido.CustomAdapter.CarrinhoAdapter;
import newpointer.com.br.newpointerpedido.CustomAdapter.PergComandaCustomDialog;
import newpointer.com.br.newpointerpedido.Model.CarrinhoModel;
import newpointer.com.br.newpointerpedido.R;

@SuppressLint("SetTextI18n")
public class CarinhoActivity extends AppCompatActivity implements View.OnClickListener, PergComandaListener {
    private ImageButton back;
    private ImageButton clear;
    private FloatingActionButton send;
    private ProgressBar prog;
    private RecyclerView lv_itens;
    private DBLiteConnection dbl;
    private Button newComanda;
    public static List<CarrinhoModel> carrinho;
    private BadgeView badge;
    private TextView qtd_itens;
    private ProgressDialog pd;
    private String comanda = "";
    private String smesa = "0";
    private Intent j;
    private int id_conta = 0;
    private String procedure_estacao = "";
    private String procedure_comanda = "";
    private String procedure_titulo = "";
    private String procedure_codoperador = "";
    private String procedure_cdprod = "";
    private String procedure_quant = "";
    private String procedure_acomp = "";
    private String procedure_obs = "";
    private String procedure_nomeoperador = "";
    private String procedure_mesa = "";
    private String procedure_valorindice = "";
    private String procedure_tpestacao = "1";

    private PergComandaCustomDialog pccd;
    private Boolean needOpenDialogAgain = false;
    private Boolean openMinhaConta = false;
    private String typedComanda = "";

    private boolean contaAberta = false;
    private boolean hasResult = false;
    private boolean sqlResponse = false;

    private Boolean perguntaMesaMode = false;
    //Numero da comanda
    private String numComanda = "";

    //Numero da mesa
    private String numMesa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinho);

        startVar();

        j = getIntent();
        comanda = j.getStringExtra("comanda");

        perguntaMesaMode = dbl.selectConfig().getPergunta_mesa() == 1;

        if (perguntaMesaMode) {
            smesa = j.getStringExtra("mesa");

            numComanda = smesa;
            numMesa = comanda;

            newComanda.setText("Adicionar novo(a) " + dbl.selectConfig().getTitulo_loja());
            newComanda.setVisibility(View.VISIBLE);
        } else {
            numComanda = comanda;

            newComanda.setVisibility(View.GONE);
        }


        carrinho = dbl.selectCarrinho();
        qtd_itens.setText("Itens: " + carrinho.size());

        lv_itens.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lv_itens.setAdapter(new CarrinhoAdapter(this, this, carrinho, perguntaMesaMode, dbl.selectConfig().getTitulo_loja()));

        prog.setVisibility(View.INVISIBLE);
        badge = MainActivity.badge;

    }

    private void startVar() {
        back = (ImageButton) findViewById(R.id.ib_carrinho_return);
        clear = (ImageButton) findViewById(R.id.ib_carrinho_clear);
        prog = (ProgressBar) findViewById(R.id.pb_carrinho_wait);
        lv_itens = findViewById(R.id.lv_carrinho);
        send = (FloatingActionButton) findViewById(R.id.fab_carrinho_send);
        qtd_itens = (TextView) findViewById(R.id.tv_carrinho_nitens);
        newComanda = findViewById(R.id.newComanda);
        dbl = new DBLiteConnection(CarinhoActivity.this);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
        clear.setOnClickListener(this);
        pd = new ProgressDialog(CarinhoActivity.this);
        newComanda.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Concluir pedido");
            builder.setMessage("Concluir pedido e enviar para cozinha?");
            builder.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarProdutos();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        }
        if (v == back) {
            finish();
        }
        if (v == clear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Apagar carrinho");
            builder.setMessage("Remover todos os itens do carrinho?");
            builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbl.deleteAllCarrinho();
                    Toast.makeText(CarinhoActivity.this, "Todos os itens foram removidos do carrinho", Toast.LENGTH_SHORT).show();
                    badge.setText("0");
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        }

        if (v == newComanda) {
            typedComanda = numMesa;
            openMinhaConta = false;
            pccd = new PergComandaCustomDialog(this, this, typedComanda, openMinhaConta, "", this);
            pccd.setCancelable(false);
            pccd.setCanceledOnTouchOutside(false);
            pccd.show();
        }
    }

    private void enviarProdutos() {
        if (pd != null) {
            pd.setMessage("Testando conexão e enviando produtos... 15");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        new EnviaProdutos().execute();
        new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (pd != null)
                    pd.setMessage("Testando conexão e enviando produtos... " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (!hasResult) {
                    if (!sqlResponse) {
                        if (pd != null) pd.hide();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Problema ao enviar produtos");
                        builder.setMessage("Ocorreu um problema ao tentar enviar os produtos para o servidor e o tempo se esgotou, verifique sua conexão com a internet e clique em tentar novamente.");
                        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                enviarProdutos();
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }
        }.start();
    }

    void checkPermissionAndStartScan() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(CarinhoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Permissão de acesso")
                        .setMessage("Para usar a camera do dispositivo, é necessário permissão de acesso. Aperte em Permitir para continuar.")
                        .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CarinhoActivity.this, new String[]{Manifest.permission.CAMERA}, 66678);
                            }
                        })
                        .setNegativeButton("Agora não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CarinhoActivity.this, "Não é possivel escanerar comanda sem permissão de camera", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            } else {
                startScan();
            }
        } else {
            startScan();
        }
    }


    private void startScan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Aponte a camera para o código de barras ou QR Code da comanda:");
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Comanda não reconhecida.", Toast.LENGTH_LONG).show();
                    if (needOpenDialogAgain) {
                        needOpenDialogAgain = false;
                        pccd = new PergComandaCustomDialog(this, this, typedComanda, openMinhaConta, "", this);
                        pccd.setCancelable(false);
                        pccd.setCanceledOnTouchOutside(false);
                        pccd.show();
                    }
                } else {
                    String barcodeResult = result.getContents();
                    if (needOpenDialogAgain) {
                        needOpenDialogAgain = false;
                        pccd = new PergComandaCustomDialog(this, this, typedComanda, openMinhaConta, barcodeResult, this);
                        pccd.setCancelable(false);
                        pccd.setCanceledOnTouchOutside(false);
                        pccd.show();
                    }
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != 66678) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage("É necessáro dar permissão de camera para acessar este recurso.")
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    @Override
    public void onOpenScanClicked() {
        needOpenDialogAgain = true;
        checkPermissionAndStartScan();
    }

    public class EnviaProdutos extends AsyncTask<String, Object, Integer> {
        private String return_procedure = "";
        private boolean isError = false;
        private String error = "";

        @Override
        protected Integer doInBackground(String... bt) {
            try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");

                if (perguntaMesaMode) {

                    String currentComanda = "";

                    for (int i = 0; i < carrinho.size(); i++) {
                        if (!carrinho.get(i).getNumComanda().equalsIgnoreCase(currentComanda)) {
                            currentComanda = carrinho.get(i).getNumComanda();

                            Log.i("TESTE", "Nova comanda encontrada, a comanda " + currentComanda);

                            procedure_cdprod = "";
                            procedure_quant = "";
                            procedure_acomp = "";
                            procedure_obs = "";

                            for (int j = 0; j < carrinho.size(); j++) {
                                CarrinhoModel item = carrinho.get(j);

                                if (item.getNumComanda().equalsIgnoreCase(currentComanda)) {
                                    Log.i("TESTE", "o envio da comanda " + currentComanda + " Tera o item " + item.getName_prod());

                                    procedure_cdprod = procedure_cdprod + item.getId_prod() + "|";
                                    procedure_quant = procedure_quant + item.getQtd_prod() + "|";
                                    procedure_acomp = procedure_acomp + item.getAcomp_prod() + "|";
                                    procedure_obs = procedure_obs + item.getObs_prod() + "|";
                                }
                            }

                            procedure_nomeoperador = dbl.selectCurrentOp().getName();
                            procedure_comanda = currentComanda;
                            procedure_mesa = numMesa;
                            procedure_codoperador = "" + dbl.selectCurrentOp().getId();
                            procedure_titulo = dbl.selectConfig().getTitulo_loja();
                            procedure_estacao = dbl.selectConfig().getEstacao();
                            procedure_tpestacao = "1";
                            procedure_valorindice = dbl.selectConfig().getTaxa() + "";

                            Log.i("prod", procedure_cdprod);
                            Log.i("qtd", procedure_quant);
                            Log.i("obs", procedure_obs);
                            Log.i("acomp", procedure_acomp);
                            Log.i("nomeop", procedure_nomeoperador);
                            Log.i("comanda", procedure_comanda);
                            Log.i("codop", procedure_codoperador);
                            Log.i("mesa", procedure_mesa);
                            Log.i("estacao", procedure_estacao);
                            Log.i("tpestacao", procedure_tpestacao);
                            Log.i("taxa", procedure_valorindice);
                            Log.i("titulo", procedure_titulo);

                            Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                            String sSql = "execute procedure GRAVA_CONSUMO_ANDROID('" + procedure_estacao + "','" + procedure_comanda + "','" + procedure_titulo + "'," +
                                    "" + procedure_codoperador + ",'" + procedure_cdprod + "','" + procedure_quant + "','" + procedure_acomp + "','" + procedure_obs + "'," +
                                    "'" + procedure_nomeoperador + "'," + procedure_tpestacao + "," + procedure_valorindice + ",'" + procedure_mesa + "')";
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery(sSql);
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            while (rs.next()) {
                                hasResult = true;
                                for (int k = 1; k <= columnsNumber; k++) {
                                    if (k > 1) System.out.print(",  ");
                                    String columnValue = rs.getString(k);
                                    System.out.print(columnValue + " " + rsmd.getColumnName(k));
                                }
                                System.out.println("");
                                if (rs.getString("RETORNO") == null) return_procedure = "null";
                                else return_procedure = rs.getString("RETORNO");
                            }
                            rs.close();
                            conn.close();
                            stmt.close();
                        }
                    }

                    return 1;

                } else {
                    int cont = 0;
                    while (cont < carrinho.size()) {
                        procedure_cdprod = procedure_cdprod + carrinho.get(cont).getId_prod() + "|";
                        procedure_quant = procedure_quant + carrinho.get(cont).getQtd_prod() + "|";
                        procedure_acomp = procedure_acomp + carrinho.get(cont).getAcomp_prod() + "|";
                        procedure_obs = procedure_obs + carrinho.get(cont).getObs_prod() + "|";
                        cont++;
                    }

                    procedure_nomeoperador = dbl.selectCurrentOp().getName();
                    procedure_comanda = comanda;
                    procedure_mesa = smesa;
                    procedure_codoperador = "" + dbl.selectCurrentOp().getId();
                    procedure_titulo = dbl.selectConfig().getTitulo_loja();
                    procedure_estacao = dbl.selectConfig().getEstacao();
                    procedure_tpestacao = "1";
                    procedure_valorindice = dbl.selectConfig().getTaxa() + "";

                    Log.i("prod", procedure_cdprod);
                    Log.i("qtd", procedure_quant);
                    Log.i("obs", procedure_obs);
                    Log.i("acomp", procedure_acomp);
                    Log.i("nomeop", procedure_nomeoperador);
                    Log.i("comanda", procedure_comanda);
                    Log.i("codop", procedure_codoperador);
                    Log.i("mesa", procedure_mesa);
                    Log.i("estacao", procedure_estacao);
                    Log.i("tpestacao", procedure_tpestacao);
                    Log.i("taxa", procedure_valorindice);
                    Log.i("titulo", procedure_titulo);

                    Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                    String sSql = "execute procedure GRAVA_CONSUMO_ANDROID('" + procedure_estacao + "','" + procedure_comanda + "','" + procedure_titulo + "'," +
                            "" + procedure_codoperador + ",'" + procedure_cdprod + "','" + procedure_quant + "','" + procedure_acomp + "','" + procedure_obs + "'," +
                            "'" + procedure_nomeoperador + "'," + procedure_tpestacao + "," + procedure_valorindice + ",'" + procedure_mesa + "')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sSql);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    while (rs.next()) {
                        hasResult = true;
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = rs.getString(i);
                            System.out.print(columnValue + " " + rsmd.getColumnName(i));
                        }
                        System.out.println("");
                        if (rs.getString("RETORNO") == null) return_procedure = "null";
                        else return_procedure = rs.getString("RETORNO");
                        //return_procedure = rs.getString("RETORNO");
                    }
                    rs.close();
                    conn.close();
                    stmt.close();
                    return 1;
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                isError = true;
                error = e1.toString();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            procedure_nomeoperador = "";
            procedure_valorindice = "";
            procedure_acomp = "";
            procedure_tpestacao = "";
            procedure_codoperador = "";
            procedure_estacao = "";
            procedure_comanda = "";
            procedure_mesa = "";
            procedure_obs = "";
            procedure_quant = "";
            procedure_valorindice = "";
            procedure_titulo = "";
            pd.hide();
            if (i == 1) {
                if (return_procedure.equalsIgnoreCase("OK")) {
                    MainActivity.fa.finish();
                    pd.hide();
                    Toast.makeText(CarinhoActivity.this, "Produtos enviados com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (return_procedure.equalsIgnoreCase("null")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Procedure returnou valor NULL. Favor comunicar o administrador");
                        builder.setMessage("Retorno da procedure: " + return_procedure);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        if (isError) {
                            builder.setNegativeButton("Exibir Erro", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                                    builder.setTitle("Log de erro");
                                    builder.setMessage(error);
                                    builder.setPositiveButton("Fechar", null);
                                    builder.setCancelable(false);
                                    builder.show();
                                }
                            });
                        }
                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Falha ao enviar produtos");
                        builder.setMessage("Retorno da procedure: " + return_procedure);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            } else {
                sqlResponse = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Falha ao conectar-se");

                builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                if (isError) {
                    builder.setNegativeButton("Exibir Erro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                            builder.setTitle("Log de erro");
                            builder.setMessage(error);
                            builder.setPositiveButton("Fechar", null);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    });
                }
                builder.setCancelable(false);
                builder.show();
            }

        }
    }
}
