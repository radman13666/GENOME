/*
 * Copyright (C) 2017 William Kibirango <williamkaos.kibirango76@gmail.com>
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
package genome.metronome.utils;

import genome.metronome.presenter.MetronomeType;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeContract {

  private MetronomeContract() {
  }
  
  public interface View {
    void initialize();
    void displayMessage(String message);
    void displayMetronomeSettings(MetronomeType type, 
                                  HashMap<String, Number> metronomeSettings);
    void clean();
  }
  
  public interface Presenter {
    void playMetronome(MetronomeType type);
    void stopMetronome(MetronomeType type);
    void initialize();
    void clean();
    void updateMetronomeSettings(MetronomeType type, 
                                 HashMap<String, Number> settings);
    Observable registerObserver(MetronomeType type, Observer ob);
  }
}
