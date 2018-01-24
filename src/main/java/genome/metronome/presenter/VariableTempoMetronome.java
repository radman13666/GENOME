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

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public abstract class VariableTempoMetronome extends Metronome {
  
  protected float startTempo;
  protected float endTempo;

  protected VariableTempoMetronome() {
    super();
  }

  public VariableTempoMetronome(float startTempo, float endTempo, int measure,
                                int subDivision) {
    super(measure, subDivision);
    setStartTempo(startTempo);
    setEndTempo(endTempo);
  }

  public final float getStartTempo() {
    return startTempo;
  }

  public final void setStartTempo(float startTempo) {
    if (startTempo >= 
        MetronomeConstants.VariableTempoMetronome.MIN_START_TEMPO && 
        startTempo <= MetronomeConstants.VariableTempoMetronome.MAX_START_TEMPO)
      this.startTempo = startTempo;
    else this.startTempo 
      = MetronomeConstants.VariableTempoMetronome.DEFAULT_START_TEMPO;
  }

  public final float getEndTempo() {
    return endTempo;
  }

  public final void setEndTempo(float endTempo) {
    if (endTempo >= MetronomeConstants.VariableTempoMetronome.MIN_END_TEMPO && 
        endTempo <= MetronomeConstants.VariableTempoMetronome.MAX_END_TEMPO)
      this.endTempo = endTempo;
    else this.endTempo 
      = MetronomeConstants.VariableTempoMetronome.DEFAULT_END_TEMPO;
  }
  
}
