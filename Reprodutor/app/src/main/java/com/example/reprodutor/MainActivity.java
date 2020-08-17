package com.example.reprodutor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //inicializando as variaveis
    Button play;
    TextView playPosicao,playDuracao;
    SeekBar seekbar;
    ImageView player,passa,volta;

    MediaPlayer midiaplay;
    Handler handler = new Handler();
    Runnable runnable;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPosicao = findViewById(R.id.posicao);
        playDuracao = findViewById(R.id.tamanho);

        player = findViewById(R.id.player);
        passa = findViewById(R.id.passar);
        volta = findViewById(R.id.voltar);

        seekbar = findViewById(R.id.seekBar);

        // inicialização do midia player
       midiaplay = MediaPlayer.create(this, R.raw.music1);
        //inicialização do Runnable
        runnable = new Runnable(){
            @Override
            public void run(){
                // Setando o progresso para acompanhar a posição
                seekbar.setProgress(midiaplay.getCurrentPosition());
                // setando um delay para a atualização da posição
                handler.postDelayed(this, 500);
            }
        };
        //Capturando a duração do da midia de audio
        int duracao = midiaplay.getDuration();
        //Convertendo milisegundo para minutos e segundos
        String sDuracao = convertFormat(duracao);
        //Setando a duração no textView
        playDuracao.setText(sDuracao);
         //função do click de player
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(midiaplay.isPlaying()){
                    // pausando o arquivo de midia play
                    midiaplay.pause();
                    // pausando  andamento da duração
                    handler.removeCallbacks(runnable);
                    //mudando a imagem parra pausado!
                    player.setImageResource(R.drawable.player);
                }else {
                    //Reproduzindo midia player
                    midiaplay.start();
                    //setando a posicão de reprodução
                    seekbar.setMax(midiaplay.getDuration());
                    //iniciando a contagem da duração e atualizando
                    handler.postDelayed(runnable, 0);
                    // mudando a imagem para ""em execução"
                    player.setImageResource(R.drawable.pause);
                }

            }
        });
        //função de click de passar >>
        passa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trocando o arquivo de audio a ser executado
            int currentPosition = midiaplay.getCurrentPosition();
            int duracao = midiaplay.getDuration();
            if (midiaplay.isPlaying() && duracao != currentPosition){
                currentPosition = currentPosition + 10000;
            }

            playPosicao.setText(convertFormat(currentPosition));
            midiaplay.seekTo(currentPosition);
            }
        });
        //função voltar
        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = midiaplay.getCurrentPosition();
                int duracao = midiaplay.getDuration();
                if (midiaplay.isPlaying() && currentPosition > 10000){
                    currentPosition = currentPosition - 10000;
                }

                playPosicao.setText(convertFormat(currentPosition));
                midiaplay.seekTo(currentPosition);
            }
        });
        //função de controle do seek bar
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // checando point
                if(fromUser){
                    //setanto poit a midia
                    midiaplay.seekTo(progress);
                }
                //setando a posição para a view
                playPosicao.setText(convertFormat(midiaplay.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        midiaplay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.setImageResource(R.drawable.replay);
                midiaplay.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duracao) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duracao),
                TimeUnit.MILLISECONDS.toSeconds(duracao)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duracao)));
    }

    // Fim
}
