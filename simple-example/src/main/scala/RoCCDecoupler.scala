package myrocc

import chisel3._
import freechips.rocketchip.tile.RoCCIO
import org.chipsalliance.cde.config.Parameters

class RoCCDecouplerIO (xLen: Int = 64)(implicit p: Parameters) extends Bundle{
  // RoCC interface
  val rocc_io = new RoCCIO(0, 0)

  // Control signals
  val clock = Input(Clock())
  val reset = Input(UInt(1.W))

  // Controller interface
  val controller_io = Flipped(new RoCCControllerIO(xLen))
}

class RoCCDecoupler (xLen: Int = 64)(implicit p: Parameters) extends Module {
  val io = new RoCCDecouplerIO()

  // Assign for global control signals
  io.controller_io.clock := io.clock
  io.controller_io.reset := io.reset

  // Process cmd
  io.controller_io.rocc_req_addr    := io.rocc_io.cmd.bits.rs1
  io.controller_io.rocc_req_wrdata  := io.rocc_io.cmd.bits.rs2
  io.controller_io.rocc_req_rd      := io.rocc_io.cmd.bits.inst.rd
  io.controller_io.rocc_req_cmd     := io.rocc_io.cmd.bits.inst.opcode
  io.controller_io.rocc_req_valid   := io.rocc_io.cmd.valid
  io.rocc_io.cmd.ready              := true.B

  // Process response
  io.rocc_io.resp.bits.rd   := io.controller_io.rocc_resp_rd
  io.rocc_io.resp.bits.data := io.controller_io.rocc_resp_data
  io.rocc_io.resp.valid     := io.controller_io.rocc_resp_valid & io.rocc_io.resp.ready

  // Disable cache access
  io.rocc_io.mem.req.valid := false.B

  // Set the other flags
  io.rocc_io.busy       := false.B
  io.rocc_io.interrupt  := io.controller_io.rocc_resp_valid

}