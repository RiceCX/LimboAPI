/*
 * Copyright (C) 2021 Elytrium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.limboauth.command;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.sql.SQLException;
import net.elytrium.limboauth.Settings;
import net.elytrium.limboauth.handler.AuthSessionHandler;
import net.elytrium.limboauth.model.RegisteredPlayer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChangePasswordCommand implements SimpleCommand {

  private final Dao<RegisteredPlayer, String> playerDao;

  public ChangePasswordCommand(Dao<RegisteredPlayer, String> playerDao) {
    this.playerDao = playerDao;
  }

  @Override
  public void execute(final Invocation invocation) {
    final CommandSource source = invocation.source();
    final String[] args = invocation.arguments();

    if (!(source instanceof Player)) {
      source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Settings.IMP.MAIN.STRINGS.NOT_PLAYER));
      return;
    }

    if (args.length != 1) {
      source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Settings.IMP.MAIN.STRINGS.CHANGE_PASSWORD_USAGE));
    } else {
      try {
        UpdateBuilder<RegisteredPlayer, String> updateBuilder = this.playerDao.updateBuilder();
        updateBuilder.where().eq("nickname", ((Player) source).getUsername());
        updateBuilder.updateColumnValue("hash", AuthSessionHandler.genHash(args[0]));
        updateBuilder.update();

        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Settings.IMP.MAIN.STRINGS.CHANGE_PASSWORD_SUCCESSFUL));
      } catch (SQLException e) {
        e.printStackTrace();

        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Settings.IMP.MAIN.STRINGS.ERROR_OCCURRED));
      }
    }
  }
}
