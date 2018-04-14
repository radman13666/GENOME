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

import genome.metronome.presenter.MetronomeHandler;
import genome.metronome.presenter.SoundRez;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class MetronomeDependencyInjector {
  
  private static MetronomeContract.Presenter metronomePresenter = null;

  private MetronomeDependencyInjector() {
  }
  
  public static MetronomeContract.Presenter 
    getMetronomePresenter(MetronomeContract.View view) {
    if (view != null) {
      if (metronomePresenter == null)
        metronomePresenter = new MetronomeHandler(view);
      return metronomePresenter;
    } else return null;
  }
  
  public static SoundRez getSoundRez() {
    return SoundRez.getInstance();
  }
}
