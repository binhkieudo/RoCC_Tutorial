module mymem (
    input           clock,
    input           rqvalid,
    input           wren,
    input [9:0]     addr,
    input [63:0]    wrdata,
    output          rdvalid,
    output [63:0]   rddata
);

    reg [63:0] mem [0:1023];

    reg [63:0] r_rddata;
    reg        r_rdvalid;

    always @(posedge clock) begin
        if (wren) mem[addr] <= wrdata;
        r_rdvalid   <= rqvalid;
        r_rddata    <= mem[addr];
    end

    assign rdvalid  = r_rdvalid;
    assign rddata   = r_rddata;

endmodule
