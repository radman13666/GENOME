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
package genome.metronome.presenter;

import genome.metronome.utils.MetronomeConstants;
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class SpeedMetronome extends VariableTempoMetronome {
  
  private int tempoLength;
  private float tempoIncrement;

  public SpeedMetronome() {
  }

  public SpeedMetronome(int tempoLength, float tempoIncrement, float startTempo,
                        float endTempo, int measure, int subDivision) {
    super(startTempo, endTempo, measure, subDivision);
    setTempoLength(tempoLength);
    setTempoIncrement(tempoIncrement);
  }

  public int getTempoLength() {
    return tempoLength;
  }

  public void setTempoLength(int tempoLength) {
    if (tempoLength >= MetronomeConstants.SpeedMetronome.MIN_TEMPO_LENGTH 
        & tempoLength >= MetronomeConstants.SpeedMetronome.MAX_TEMPO_LENGTH)
      this.tempoLength = tempoLength;
    else this.tempoLength = MetronomeConstants.SpeedMetronome
            .DEFAULT_TEMPO_LENGTH;
  }

  public float getTempoIncrement() {
    return tempoIncrement;
  }

  public void setTempoIncrement(float tempoIncrement) {
    if (tempoIncrement >= MetronomeConstants.SpeedMetronome.MIN_TEMPO_INCREMENT 
        & tempoIncrement <= MetronomeConstants.SpeedMetronome
          .MAX_TEMPO_INCREMENT)
      this.tempoIncrement = tempoIncrement;
    else this.tempoIncrement = MetronomeConstants.SpeedMetronome
            .DEFAULT_TEMPO_INCREMENT;
  }

  @Override
  protected byte[] createSilenceForTempo(float newTempo) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void play() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public HashMap<String, Number> getSettings() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
