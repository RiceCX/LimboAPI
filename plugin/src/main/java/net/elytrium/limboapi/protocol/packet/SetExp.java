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

package net.elytrium.limboapi.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;

public class SetExp implements MinecraftPacket {

  private float expBar;
  private int level;
  private int totalExp;

  public SetExp(float expBar, int level, int totalExp) {
    this.expBar = expBar;
    this.level = level;
    this.totalExp = totalExp;
  }

  public SetExp() {

  }

  @Override
  public void decode(ByteBuf byteBuf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {

  }

  @Override
  public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
    if (protocolVersion.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
      buf.writeFloat(this.expBar);
      ProtocolUtils.writeVarInt(buf, this.level);
      ProtocolUtils.writeVarInt(buf, this.totalExp);
    } else {
      buf.writeByte((int) this.expBar); // or? buf.writeFloat(this.expBar);
      buf.writeByte(this.level); // or? buf.writeShort(this.level);
      buf.writeShort(this.totalExp);
    }
  }

  @Override
  public boolean handle(MinecraftSessionHandler handler) {
    return true;
  }

  public float getExpBar() {
    return this.expBar;
  }

  public int getLevel() {
    return this.level;
  }

  public int getTotalExp() {
    return this.totalExp;
  }

  public void setExpBar(float expBar) {
    this.expBar = expBar;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setTotalExp(int totalExp) {
    this.totalExp = totalExp;
  }

  @Override
  public String toString() {
    return "SetExp{"
        + "expBar=" + this.getExpBar()
        + ", level=" + this.getLevel()
        + ", totalExp=" + this.getTotalExp()
        + "}";
  }
}
