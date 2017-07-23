//package com.example.akshit.project_washroom_signs;
//
//import android.speech.tts.Voice;
//
//import java.util.Locale;
//import com.sun.speech.freetts.jsapi.FreeTTSSynthesizer;
//import java.beans.PropertyVetoException;
//import java.util.Locale;//
//import javax.speech.AudioException;
//import javax.speech.Central;
//import javax.speech.EngineException;
//import javax.speech.synthesis.SynthesizerModeDesc;
////import javax.speech.synthesis.Voice;
///**
// * Created by Akshit on 24-05-2017.
// */
//
//public class TextToSpeak {
//    public void Speak(String output){
//        String voiceName ="kevin16";
//        String a=output;
//        try
//        {
//            System.setProperty("FreeTTSSynthEngineCentral", "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); ///// To Set system property
//            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); ///// To set system property
//            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");   /// To register Engine Central
//            SynthesizerModeDesc desc = new SynthesizerModeDesc(null,null,  Locale.US,null,null);
//            FreeTTSSynthesizer synthesizer = (FreeTTSSynthesizer) Central.createSynthesizer(desc); ///Create Synthesizer
//            synthesizer.allocate();                               ///Allocation of synthesizer
//            synthesizer.resume();
//            desc = (SynthesizerModeDesc)  synthesizer.getEngineModeDesc();    //To get EngineMode
//
//            javax.speech.synthesis.Voice[] voices = desc.getVoices();
//            javax.speech.synthesis.Voice voice = null;                                               //To get Available Voices
//            for (javax.speech.synthesis.Voice voice1 : voices)
//            {
//                if (voice1.getName().equals(voiceName))
//                {
//                    voice = voice1;
//                    break;
//                }
//            }
//            synthesizer.getSynthesizerProperties().setVoice(voice);               //To set Available voice
//            synthesizer.getSynthesizerProperties().setSpeakingRate(100);          //To set voice speed
//            synthesizer.speakPlainText(a,null);
//
//
//        }
//       /* catch (EngineException | IllegalArgumentException | AudioException | SecurityException | PropertyVetoException e)
//        {
//            String message = " missing speech.properties in " + System.getProperty("user.home") + "\n";
//            System.out.println(e.getClass().getName());
//            System.out.println(message);
//        }*/
//        catch(Exception e)
//        {}
//
//    }
//
//}
