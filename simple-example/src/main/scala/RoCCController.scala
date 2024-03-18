package myrocc

import chisel3._
import org.chipsalliance.cde.config.Parameters

class RoCCControllerIO (xLen: Int = 64)(implicit p: Parameters) extends Bundle{
  // Request
  val rocc_req_addr     = Input(UInt(xLen.W))
  val rocc_req_wrdata   = Input(UInt(xLen.W))
  val rocc_req_rd       = Input(UInt(5.W))
  val rocc_req_cmd      = Input(UInt(7.W))
  val rocc_req_valid    = Input(Bool())
  val rocc_req_ready    = Output(Bool())

  // Response
  val rocc_resp_rd      = Output(UInt(5.W))
  val rocc_resp_data    = Output(UInt(xLen.W))
  val rocc_resp_valid   = Output(Bool())

  // Control signals
  val clock = Input(Clock())
  val reset = Input(UInt(1.W))

  // Black box IO
  val bb_io = Flipped(new mymemIO(xLen))
}

class RoCCController (xLen: Int = 64)(implicit p: Parameters) extends Module {
  val io = new RoCCControllerIO(xLen)

  io.bb_io.clock    := io.clock
  io.bb_io.rqvalid  := io.rocc_req_valid
  io.bb_io.wren     := (io.rocc_req_cmd === ISA.WRITE)
  io.bb_io.addr     := io.rocc_req_addr
  io.bb_io.wrdata   := io.rocc_req_wrdata

  io.rocc_resp_rd    := io.rocc_req_rd
  io.rocc_req_ready  := true.B
  io.rocc_resp_data  := io.bb_io.rddata
  io.rocc_resp_valid := io.bb_io.rdvalid
}
