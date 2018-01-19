/*
 * Copyright (C) 2018 William Kibirango <williamkaos.kibirango76@gmail.com>
 *
 * This file is part of GENOME.
 *
 * GENOME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GENOME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GENOME.  If not, see <http://www.gnu.org/licenses/>.
 */
package genome.metronome.view;

import genome.metronome.presenter.MetronomeType;
import genome.metronome.utils.MetronomeConstants;
import genome.metronome.utils.MetronomeContract;
import genome.metronome.utils.MetronomeDependencyInjector;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public class GenomeCLI implements MetronomeContract.View/*, Observer*/ {

  private MetronomeContract.Presenter p;
//  private TimedMetronome tM; 
//  private GapMetronome gM; 
//  private SpeedMetronome sM;
  
  private float t, ti, st, et;
  private int lm, sm, gli, gr, m, tl, d;
  private static final String HELP_MESSAGE = 
    "Usage: java GenomeCLI [\"<option> <settings>\"... | H | <no args>]\n" + 
    "\t<option>     --- G, T or S\n" + 
    "\t<settings>   --- if <option> is G, [lm] [sm] [gli] [gr] [t] [m] [d]\n" +
    "\t             --- if <option> is T, [d] [t] [m]\n" + 
    "\t             --- if <option> is S, [tl] [ti] [st] [et] [m]\n" + 
    "\tH, <no args> --- display this help message and exit";

  @Override
  public void initialize() {
    displayMessage("===================================\n" + 
                   "GENOME (cli): Rhythm's inborn");
    p = MetronomeDependencyInjector.getMetronomePresenter(this);
    p.initialize();
//    sM = (SpeedMetronome) p.registerObserver(MetronomeType.SPEED, this);
//    tM = (TimedMetronome) p.registerObserver(MetronomeType.TIMED, this);
//    gM = (GapMetronome) p.registerObserver(MetronomeType.GAP, this);
  }

  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  @Override
  public void displayMetronomeSettings(MetronomeType type,
                                   HashMap<String, Number> metronomeSettings) {
    displayMessage("===================================\n");
    switch (type) {
      case GAP:
        displayMessage("==> GAP:\n");
        displayMessage("lm: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.LOUD_MEASURES));
        displayMessage("sm: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.SILENT_MEASURES));
        displayMessage("gli: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.GAP_LENGTH_INCREMENT));
        displayMessage("gr: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.GAP_REPETITIONS));
        displayMessage("t: " + (Float) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.TEMPO));
        displayMessage("m: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        displayMessage("s: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        displayMessage("d: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.DURATION));
        break;
      case TIMED:
        displayMessage("==> TIMED:\n");
        displayMessage("t: " + (Float) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.TEMPO));
        displayMessage("m: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        displayMessage("s: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        displayMessage("d: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.DURATION));
        break;
      case SPEED:
        displayMessage("==> SPEED:\n");
        displayMessage("tl: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH));
        displayMessage("ti: " + (Float) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT));
        displayMessage("st: " + (Float) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.START_TEMPO));
        displayMessage("et: " + (Float) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.END_TEMPO));
        displayMessage("m: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.MEASURE));
        displayMessage("s: " + (Integer) metronomeSettings.get(
          MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION));
        break;
      default: break;
    }
    displayMessage("\n===================================\n");
  }

  @Override
  public void clean() {
    p.clean();
    displayMessage("===================================\n" + 
                   "Done. Exiting...\n" + 
                   "===================================");
  }

//  @Override
//  public void update(Observable o, Object arg) {
//    String updateStr = "==> UPDATE: ";
//    if (o instanceof TimedMetronome &&
//        o == tM &&
//        ((String) arg).equals(
//            MetronomeConstants.Metronome.AudioTasks.TM_CURRENT_TIME_LEFT)) {
//      updateStr += "TIMED: timeLeft (mins) = " + 
//                   ((TimedMetronome) o).getCurrentTimeLeft() + "\n";
//      displayMessage(updateStr);
//    } else if (o instanceof SpeedMetronome &&
//               o == sM &&
//               ((String) arg).equals(
//                MetronomeConstants.Metronome.AudioTasks.SM_CURRENT_TEMPO)) {
//      updateStr += "SPEED: currentTempo (bpm) = " +
//                   ((SpeedMetronome) o).getCurrentTempo() + "\n";
//      displayMessage(updateStr);
//    } else if (o instanceof GapMetronome &&
//               o == gM &&
//               ((String) arg).equals(
//                MetronomeConstants.Metronome.AudioTasks
//                  .GM_CURRENT_SILENT_MEASURES)) {
//      updateStr += "GAP: lm = " +
//                   ((GapMetronome) o).getLoudMeasures() + " sm = " +
//                   ((GapMetronome) o).getCurrentSilentMeasures() + "\n";
//      displayMessage(updateStr);
//    }
//  }
  
  private boolean isValid(String arg) throws Exception {
    try (Scanner line = new Scanner(arg);) {
      String mode = line.next(), regex = "\\s+";
      
      switch (mode) {
        case "G":
          if (arg.split(regex).length != 8) return false;
          lm = line.nextInt(); sm = line.nextInt(); gli = line.nextInt();
          gr = line.nextInt(); t = line.nextFloat(); m = line.nextInt();
          d = line.nextInt();
          return true;
        case "T":
          if (arg.split(regex).length != 4) return false;
          d = line.nextInt(); t = line.nextFloat(); m = line.nextInt();
          return true;
        case "S":
          if (arg.split(regex).length != 6) return false;
          tl = line.nextInt(); ti = line.nextFloat(); st = line.nextFloat();
          et = line.nextFloat(); m = line.nextInt();
          return true;
        default: return false;
      }
    }
  }
  
  private void play(char mode) {
    HashMap<String, Number> settings = new HashMap<>();
    settings.put(MetronomeConstants.MetronomeSettingsKeys
          .SUB_DIVISION, MetronomeConstants.Metronome.NO_SUB_DIVISION);
    
    switch (mode) {
      case 'G':
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .LOUD_MEASURES, lm);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .SILENT_MEASURES, sm);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .GAP_LENGTH_INCREMENT, gli);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .GAP_REPETITIONS, gr);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .TEMPO, t);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .MEASURE, m);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .DURATION, d);
        p.updateMetronomeSettings(MetronomeType.GAP, settings);
        displayMessage("==> Playing Gap Metronome...\n");
        p.playMetronome(MetronomeType.GAP);
        p.stopMetronome(MetronomeType.GAP);
        displayMessage("==> Stopping Gap Metronome...\n");
        break;
      case 'T':
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .TEMPO, t);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .MEASURE, m);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .DURATION, d);
        p.updateMetronomeSettings(MetronomeType.TIMED, settings);
        displayMessage("==> Playing Timed Metronome...\n");
        p.playMetronome(MetronomeType.TIMED);
        p.stopMetronome(MetronomeType.TIMED);
        displayMessage("==> Stopping Timed Metronome...\n");
        break;
      case 'S':
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .TEMPO_LENGTH, tl);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .TEMPO_INCREMENT, ti);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .START_TEMPO, st);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .END_TEMPO, et);
        settings.put(MetronomeConstants.MetronomeSettingsKeys
          .MEASURE, m);
        p.updateMetronomeSettings(MetronomeType.SPEED, settings);
        displayMessage("==> Playing Speed Metronome...\n");
        p.playMetronome(MetronomeType.SPEED);
        p.stopMetronome(MetronomeType.SPEED);
        displayMessage("==> Stopping Speed Metronome...\n");
        break;
      default: break;
    }
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0 || args[0].equals("H")) {
      System.out.println(HELP_MESSAGE);
      System.exit(0);
    } else {
      GenomeCLI g = new GenomeCLI();
      g.initialize();
      for (String arg : args) {
        String trimmedArg = arg.trim();
        try {
          if (g.isValid(trimmedArg)) {
            g.play(trimmedArg.charAt(0));
          } else g.displayMessage("==> BAD ARG: " + arg + 
                                    ".\n    Going to next arg...\n");
        } catch (Exception e) {
          g.displayMessage("==> BAD ARG: " + arg + 
                             ".\n    Going to next arg...\n");
        }
      }
      g.clean();
    }
  }
  
}
