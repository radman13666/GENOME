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
public final class SoundSettings {
  
  private String accentSound;
  private String beatSound;
  private String clickSound;
  private String tempoChangeSound;

  public SoundSettings() {
  }

  public SoundSettings(String accentSound, String beatSound, String clickSound,
                       String tempoChangeSound) {
    setAccentSound(accentSound);
    setBeatSound(beatSound);
    setClickSound(clickSound);
    setTempoChangeSound(tempoChangeSound);
  }

  public String getAccentSound() {
    return accentSound;
  }

  public void setAccentSound(String accentSound) {
    this.accentSound = accentSound;
  }

  public String getBeatSound() {
    return beatSound;
  }

  public void setBeatSound(String beatSound) {
    this.beatSound = beatSound;
  }

  public String getClickSound() {
    return clickSound;
  }

  public void setClickSound(String clickSound) {
    this.clickSound = clickSound;
  }

  public String getTempoChangeSound() {
    return tempoChangeSound;
  }

  public void setTempoChangeSound(String tempoChangeSound) {
    this.tempoChangeSound = tempoChangeSound;
  }
  
}
