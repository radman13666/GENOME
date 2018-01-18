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
public abstract class ConstantTempoMetronome extends Metronome {
  
  protected float tempo;

  protected ConstantTempoMetronome() {
  }

  public ConstantTempoMetronome(float tempo, int measure, int subDivision) {
    super(measure, subDivision);
    setTempo(tempo);
  }

  public final float getTempo() {
    return tempo;
  }

  public final void setTempo(float tempo) {
    if (tempo >= MetronomeConstants.ConstantTempoMetronome.MIN_TEMPO &&
        tempo <= MetronomeConstants.ConstantTempoMetronome.MAX_TEMPO)
      this.tempo = tempo;
    else this.tempo = MetronomeConstants.ConstantTempoMetronome.DEFAULT_TEMPO;
  }
  
}
