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
import java.util.InputMismatchException;
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
  private static int breakPeriod;
  private static final String HELP_MESSAGE = 
    "Usage: GENOME [<break period> \"<option> <settings>\"..."
    + " | H | <no args>]\n\n" + 
    "  <break period> --- the minutes to pause between metronome playing\n" +
    "        <option> --- G, T or S\n" + 
    "      <settings> --- if <option> is G, [lm] [sm] [gli] [gr] [t] [m] [d]\n"+
    "                 --- if <option> is T, [d] [t] [m]\n" + 
    "                 --- if <option> is S, [tl] [ti] [st] [et] [m]\n" + 
    "    H, <no args> --- display this help message and exit\n\n"
    + "            [lm] --- loud measures\n"
    + "            [sm] --- silent measures\n"
    + "           [gli] --- gap length increment\n"
    + "            [gr] --- gap repetitions\n"
    + "             [t] --- tempo\n"
    + "            [st] --- start tempo\n"
    + "            [et] --- end tempo\n"
    + "             [m] --- measure\n"
    + "             [d] --- duration\n"
    + "            [tl] --- tempo length\n"
    + "            [ti] --- tempo increment\n\n"
    + "  Example 1: To play a timed metronome at 170 bpm, common time for "
    + "5 minutes\n"
    + "    $ GENOME 0 \"T 5 170 4\"\n\n"
    + "  Example 2: To play a series of different metronomes, with 5-minutes "
    + "break inbetween\n"
    + "    $ GENOME 5 \"T 10 120 8\" \"G 4 4 2 4 100 8 10\" "
    + "\"S 4 5 100 160 8\"\n\n";

  @Override
  public void initialize() {
    displayMessage("===================================\n" + 
                   "GENOME Copyright (C) 2018 William Kibirango\n");
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
  
  private boolean isValid(String arg) throws InputMismatchException {
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
  
  private void play(char mode) throws InterruptedException {
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
        if (breakPeriod == 0) 
          displayMessage("==> Stopping Gap Metronome...\n");
        else
          displayMessage("==> Stopping Gap Metronome...\n" + 
                       "    Take a " + breakPeriod + "-minute(s) break...\n");
        Thread.sleep(breakPeriod * 60_000);
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
        if (breakPeriod == 0) 
          displayMessage("==> Stopping Timed Metronome...\n");
        else
          displayMessage("==> Stopping Timed Metronome...\n" + 
                       "    Take a " + breakPeriod + "-minute(s) break...\n");
        Thread.sleep(breakPeriod * 60_000);
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
        if (breakPeriod == 0) 
          displayMessage("==> Stopping Speed Metronome...\n");
        else
          displayMessage("==> Stopping Speed Metronome...\n" + 
                       "    Take a " + breakPeriod + "-minute(s) break...\n");
        Thread.sleep(breakPeriod * 60_000);
        break;
      default: break;
    }
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length <= 1 ||
        (args.length > 1 && args[0].length() > 1) ||
        (args.length > 1 && args[0].length() == 1 &&
         !Character.isDigit((Character) args[0].charAt(0)))) {
      System.out.println(HELP_MESSAGE);
      System.exit(-1);
    } else {
      try {
        breakPeriod = (Integer.parseInt(args[0]) <= 9 &&
                    Integer.parseInt(args[0]) >= 0) ? 
                    Integer.parseInt(args[0]) : 
                    5;
        GenomeCLI g = new GenomeCLI();
        g.initialize();
        for (int i = 1; i < args.length; i++) {
          String trimmedArg = args[i].trim();
          try {
            if (g.isValid(trimmedArg)) {
              g.play(trimmedArg.charAt(0));
            } else g.displayMessage("==> BAD ARG: " + args[i] + 
                                      ".\n    Going to next arg...\n");
          } catch (InputMismatchException e) {
            g.displayMessage("==> BAD ARG: " + args[i] + 
                               ".\n    Going to next arg...\n");
          } catch (InterruptedException e) {
            break;
          }
        }
        g.clean();
      } catch (NumberFormatException e) {
        System.out.println(HELP_MESSAGE);
        System.exit(-1);
      }
    }
  }
  
}
