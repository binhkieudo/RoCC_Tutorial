# Tutorial 1: A Simple RoCC Interface

This tutorial shows how to deploy the simple-example to chipyard framework.

This example illustrates how to use RoCCIO to communicate with a tightly-coupled accelerator.

In this example, the accelerator is a Block Memory (BRAM) which is declared by Verilog. FPU and Memory interface are ignored.

<img src="https://github.com/binhkieudo/RoCC_Tutorial/assets/22954544/eebf9387-0c67-453c-b4d4-0b640c5ead10" alt="drawing" width="600"/>

The test software will write data to RoCC-BRAM and then read back.
