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
package genome.metronome.model;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class TimedMetronomeSettings 
        extends ConstantTempoMetronomeSettings {
  
  private int duration;

  public TimedMetronomeSettings() {
  }

  public TimedMetronomeSettings(int duration, float tempo, int measure,
                                int subDivision) {
    super(tempo, measure, subDivision);
    setDuration(duration);
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
