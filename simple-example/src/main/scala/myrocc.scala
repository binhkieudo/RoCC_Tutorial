package myrocc

import chisel3._
import chisel3.util.HasBlackBoxResource
import freechips.rocketchip.tile.{LazyRoCC, LazyRoCCModuleImp, OpcodeSet}
import org.chipsalliance.cde.config.Parameters

//case object myroccXLen extends Field[Int]

class mymemIO (xLen: Int = 64)(implicit p: Parameters) extends Bundle {
  val clock   = Input(Clock())
  val rqvalid = Input(Bool())
  val wren    = Input(Bool())
  val addr    = Input(UInt(10.W))
  val wrdata  = Input(UInt(xLen.W))
  val rdvalid = Output(Bool())
  val rddata  = Output(UInt(xLen.W))
}

class myrocc_bb (xLen: Int = 64)(implicit p: Parameters) extends BlackBox with HasBlackBoxResource {
  val io = IO(new mymemIO(xLen))

  addResource("/vsrc/mymem.v")
}

class myroccAccel (opcodes: OpcodeSet)(implicit p: Parameters) extends LazyRoCC (
  opcodes   = opcodes,
  nPTWPorts = 0) {
  override lazy val module = new myroccAccelImp(this)
}

class myroccAccelImp(outer: myroccAccel)(implicit p: Parameters) extends LazyRoCCModuleImp(outer) {
  val xLen = 64

  // Instantiate the rocc modules
  val myroccDecoupler  = Module(new RoCCDecoupler(xLen))

  val myroccController = Module(new RoCCController(xLen))

  val myroccBlackBox   = Module(new myrocc_bb(xLen))

  // Connect
  io <> myroccDecoupler.io.rocc_io

  myroccDecoupler.io.controller_io <> myroccController.io

  myroccController.io.bb_io <> myroccBlackBox.io

}