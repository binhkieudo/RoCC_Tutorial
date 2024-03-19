package tut_1

import chisel3._
import freechips.rocketchip.tile.RoCCIO
import org.chipsalliance.cde.config.Parameters



class RoCCDecouplerIO (xLen: Int = 64)(implicit p: Parameters) extends Bundle{
  // Control signals
  val clock = Input(Clock())
  val reset = Input(UInt(1.W))

  // RoCC interface
  val rocc_io = new RoCCIO(0, 0)

  // Decoupler-Controller interface
  val controller_io = Flipped(new ControllerDecouplerIO(xLen))
}

class RoCCDecoupler (xLen: Int = 64)(implicit p: Parameters) extends Module {
  val io = IO(new RoCCDecouplerIO())

  // Process cmd
  io.controller_io.rocc_req_addr    := io.rocc_io.cmd.bits.rs1
  io.controller_io.rocc_req_wrdata  := io.rocc_io.cmd.bits.rs2
  io.controller_io.rocc_req_rd      := io.rocc_io.cmd.bits.inst.rd
  io.controller_io.rocc_req_cmd     := io.rocc_io.cmd.bits.inst.opcode
  io.controller_io.rocc_req_valid   := io.rocc_io.cmd.valid
  io.rocc_io.cmd.ready              := io.controller_io.rocc_req_ready

  // Process response
  io.rocc_io.resp.bits.rd   := io.controller_io.rocc_resp_rd
  io.rocc_io.resp.bits.data := io.controller_io.rocc_resp_data
  io.rocc_io.resp.valid     := io.controller_io.rocc_resp_valid

  // Disable memory request
  io.rocc_io.mem.req.valid          := false.B
  io.rocc_io.mem.req.bits.addr      := 0.U
  io.rocc_io.mem.req.bits.tag       := 0.U
  io.rocc_io.mem.req.bits.cmd       := 0.U
  io.rocc_io.mem.req.bits.size      := 0.U
  io.rocc_io.mem.req.bits.signed    := false.B
  io.rocc_io.mem.req.bits.dprv      := 0.U
  io.rocc_io.mem.req.bits.dv        := false.B
  io.rocc_io.mem.req.bits.phys      := false.B
  io.rocc_io.mem.req.bits.no_alloc  := false.B
  io.rocc_io.mem.req.bits.no_xcpt   := false.B
  io.rocc_io.mem.req.bits.data      := 0.U
  io.rocc_io.mem.req.bits.mask      := 0.U
  io.rocc_io.mem.s1_kill            := false.B
  io.rocc_io.mem.s1_data.data       := 0.U
  io.rocc_io.mem.s1_data.mask       := 0.U
  io.rocc_io.mem.s2_kill            := false.B
  io.rocc_io.mem.keep_clock_enabled := true.B

  // Set the other flags
  io.rocc_io.busy       := false.B
  io.rocc_io.interrupt  := false.B

  // Disable FPU request
  io.rocc_io.fpu_req.valid            := false.B
  io.rocc_io.fpu_req.bits.rm          := 0.U
  io.rocc_io.fpu_req.bits.fmaCmd      := 0.U
  io.rocc_io.fpu_req.bits.typ         := 0.U
  io.rocc_io.fpu_req.bits.fmt         := 0.U
  io.rocc_io.fpu_req.bits.in1         := 0.U
  io.rocc_io.fpu_req.bits.in2         := 0.U
  io.rocc_io.fpu_req.bits.in3         := 0.U
  io.rocc_io.fpu_req.bits.ldst        := false.B
  io.rocc_io.fpu_req.bits.wen         := false.B
  io.rocc_io.fpu_req.bits.ren1        := false.B
  io.rocc_io.fpu_req.bits.ren2        := false.B
  io.rocc_io.fpu_req.bits.ren3        := false.B
  io.rocc_io.fpu_req.bits.swap12      := false.B
  io.rocc_io.fpu_req.bits.swap23      := false.B
  io.rocc_io.fpu_req.bits.typeTagIn   := 0.U
  io.rocc_io.fpu_req.bits.typeTagOut  := 0.U
  io.rocc_io.fpu_req.bits.fromint     := false.B
  io.rocc_io.fpu_req.bits.toint       := false.B
  io.rocc_io.fpu_req.bits.fastpipe    := false.B
  io.rocc_io.fpu_req.bits.fma         := false.B
  io.rocc_io.fpu_req.bits.div         := false.B
  io.rocc_io.fpu_req.bits.sqrt        := false.B
  io.rocc_io.fpu_req.bits.wflags      := false.B

  io.rocc_io.fpu_resp.ready       := false.B

}
