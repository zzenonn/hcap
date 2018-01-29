package edu.ateneo.nrg.kaitai.network;

import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;

/**
 * @author zenon
 * 
 * This file was generated by the kaitai struct compiler.
 * Although it may be edited to suit your needs, please
 * refer to @see <a href="http://kaitai.io/</a> for more
 * details on the kaitai struct library and how it can be 
 * used to parse binary files.
 * 
 * The ksy file was based on one of the formats in the
 * ksy format gallery @see <a href="http://formats.kaitai.io/"</a>.
 * However, parts of the ksy or the java file may have
 * been edited for the purposes of this project.
 */
public class Ipv6Packet extends KaitaiStruct {
    public static Ipv6Packet fromFile(String fileName) throws IOException {
        return new Ipv6Packet(new KaitaiStream(fileName));
    }

    public enum ProtocolEnum {
        HOPOPT(0),
        ICMP(1),
        IGMP(2),
        GGP(3),
        IPV4(4),
        ST(5),
        TCP(6),
        CBT(7),
        EGP(8),
        IGP(9),
        BBN_RCC_MON(10),
        NVP_II(11),
        PUP(12),
        ARGUS(13),
        EMCON(14),
        XNET(15),
        CHAOS(16),
        UDP(17),
        MUX(18),
        DCN_MEAS(19),
        HMP(20),
        PRM(21),
        XNS_IDP(22),
        TRUNK_1(23),
        TRUNK_2(24),
        LEAF_1(25),
        LEAF_2(26),
        RDP(27),
        IRTP(28),
        ISO_TP4(29),
        NETBLT(30),
        MFE_NSP(31),
        MERIT_INP(32),
        DCCP(33),
        X_3PC(34),
        IDPR(35),
        XTP(36),
        DDP(37),
        IDPR_CMTP(38),
        TP_PLUS_PLUS(39),
        IL(40),
        IPV6(41),
        SDRP(42),
        IPV6_ROUTE(43),
        IPV6_FRAG(44),
        IDRP(45),
        RSVP(46),
        GRE(47),
        DSR(48),
        BNA(49),
        ESP(50),
        AH(51),
        I_NLSP(52),
        SWIPE(53),
        NARP(54),
        MOBILE(55),
        TLSP(56),
        SKIP(57),
        IPV6_ICMP(58),
        IPV6_NONXT(59),
        IPV6_OPTS(60),
        ANY_HOST_INTERNAL_PROTOCOL(61),
        CFTP(62),
        ANY_LOCAL_NETWORK(63),
        SAT_EXPAK(64),
        KRYPTOLAN(65),
        RVD(66),
        IPPC(67),
        ANY_DISTRIBUTED_FILE_SYSTEM(68),
        SAT_MON(69),
        VISA(70),
        IPCV(71),
        CPNX(72),
        CPHB(73),
        WSN(74),
        PVP(75),
        BR_SAT_MON(76),
        SUN_ND(77),
        WB_MON(78),
        WB_EXPAK(79),
        ISO_IP(80),
        VMTP(81),
        SECURE_VMTP(82),
        VINES(83),
        IPTM(84),
        NSFNET_IGP(85),
        DGP(86),
        TCF(87),
        EIGRP(88),
        OSPFIGP(89),
        SPRITE_RPC(90),
        LARP(91),
        MTP(92),
        AX_25(93),
        IPIP(94),
        MICP(95),
        SCC_SP(96),
        ETHERIP(97),
        ENCAP(98),
        ANY_PRIVATE_ENCRYPTION_SCHEME(99),
        GMTP(100),
        IFMP(101),
        PNNI(102),
        PIM(103),
        ARIS(104),
        SCPS(105),
        QNX(106),
        A_N(107),
        IPCOMP(108),
        SNP(109),
        COMPAQ_PEER(110),
        IPX_IN_IP(111),
        VRRP(112),
        PGM(113),
        ANY_0_HOP(114),
        L2TP(115),
        DDX(116),
        IATP(117),
        STP(118),
        SRP(119),
        UTI(120),
        SMP(121),
        SM(122),
        PTP(123),
        ISIS_OVER_IPV4(124),
        FIRE(125),
        CRTP(126),
        CRUDP(127),
        SSCOPMCE(128),
        IPLT(129),
        SPS(130),
        PIPE(131),
        SCTP(132),
        FC(133),
        RSVP_E2E_IGNORE(134),
        MOBILITY_HEADER(135),
        UDPLITE(136),
        MPLS_IN_IP(137),
        MANET(138),
        HIP(139),
        SHIM6(140),
        WESP(141),
        ROHC(142),
        RESERVED_255(255);

        private final long id;
        ProtocolEnum(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ProtocolEnum> byId = new HashMap<Long, ProtocolEnum>(144);
        static {
            for (ProtocolEnum e : ProtocolEnum.values())
                byId.put(e.id(), e);
        }
        public static ProtocolEnum byId(long id) { return byId.get(id); }
    }

    public Ipv6Packet(KaitaiStream _io) {
        super(_io);
        this._root = this;
        _read();
    }

    public Ipv6Packet(KaitaiStream _io, KaitaiStruct _parent) {
        super(_io);
        this._parent = _parent;
        this._root = this;
        _read();
    }

    public Ipv6Packet(KaitaiStream _io, KaitaiStruct _parent, Ipv6Packet _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    private void _read() {
        this.version = this._io.readBitsInt(4);
        this.trafficClass = this._io.readBitsInt(8);
        this.flowLabel = (int) this._io.readBitsInt(20);
        this._io.alignToByte();
        this.payloadLength = this._io.readU2be();
        this.nextHeaderType = ProtocolEnum.byId(this._io.readU1());
        this.hopLimit = this._io.readU1();
        this.srcIpv6Addr = this._io.readBytes(16);
        this.dstIpv6Addr = this._io.readBytes(16);
        switch (nextHeaderType()) {
        case TCP: {
        	if(!this._io.isEof())	
        	{
        		try {
					this.nextHeader = new TcpSegment(this._io);
				} catch (Exception e) {
					this.nextHeader = null;
				}
        	}
            break;
        }
        case IPV6_NONXT: {
        	if(!this._io.isEof())
        	{
        		try {
					this.nextHeader = new NoNextHeader(this._io, this, _root);
				} catch (Exception e) {
					this.nextHeader = null;
				}
        	}
            break;
        }
        case UDP: {
        	if(!this._io.isEof())
        	{
        		try {
					this.nextHeader = new UdpDatagram(this._io);
				} catch (Exception e) {
					this.nextHeader = null;
				}
        	}
            break;
        }
        case HOPOPT: {
        	if(!this._io.isEof())
        	{
        		try {
					this.nextHeader = new OptionHopByHop(this._io, this, _root);
				} catch (Exception e) {
					this.nextHeader = null;
				}
        	}
            break;
        }
//        case IPV4: {
//        	if(!this._io.isEof())
//        		this.nextHeader = new Ipv4Packet(this._io);
//            break;
//        }
        }
        this.rest = this._io.readBytesFull();
    }
    public static class NoNextHeader extends KaitaiStruct {
        public static NoNextHeader fromFile(String fileName) throws IOException {
            return new NoNextHeader(new KaitaiStream(fileName));
        }

        public NoNextHeader(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public NoNextHeader(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public NoNextHeader(KaitaiStream _io, KaitaiStruct _parent, Ipv6Packet _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
        }
        private Ipv6Packet _root;
        private KaitaiStruct _parent;
        public Ipv6Packet _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class OptionHopByHop extends KaitaiStruct {
        public static OptionHopByHop fromFile(String fileName) throws IOException {
            return new OptionHopByHop(new KaitaiStream(fileName));
        }

        public OptionHopByHop(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public OptionHopByHop(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public OptionHopByHop(KaitaiStream _io, KaitaiStruct _parent, Ipv6Packet _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.nextHeaderType = this._io.readU1();
            this.hdrExtLen = this._io.readU1();
            this.body = this._io.readBytes((hdrExtLen() - 1));
            switch (nextHeaderType()) {
            case 0: {
                this.nextHeader = new OptionHopByHop(this._io, this, _root);
                break;
            }
            case 6: {
                this.nextHeader = new TcpSegment(this._io);
                break;
            }
            case 59: {
                this.nextHeader = new NoNextHeader(this._io, this, _root);
                break;
            }
            }
        }
        private int nextHeaderType;
        private int hdrExtLen;
        private byte[] body;
        private KaitaiStruct nextHeader;
        private Ipv6Packet _root;
        private KaitaiStruct _parent;
        public int nextHeaderType() { return nextHeaderType; }
        public int hdrExtLen() { return hdrExtLen; }
        public byte[] body() { return body; }
        public KaitaiStruct nextHeader() { return nextHeader; }
        public Ipv6Packet _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    private long version;
    private long trafficClass;
    private int flowLabel;
    private int payloadLength;
    private ProtocolEnum nextHeaderType;
    private int hopLimit;
    private byte[] srcIpv6Addr;
    private byte[] dstIpv6Addr;
    private KaitaiStruct nextHeader;
    private byte[] rest;
    private Ipv6Packet _root;
    private KaitaiStruct _parent;
    public long version() { return version; }
    public long trafficClass() { return trafficClass; }
    public int flowLabel() { return flowLabel; }
    public int payloadLength() { return payloadLength; }
    public ProtocolEnum nextHeaderType() { return nextHeaderType; }
    public int hopLimit() { return hopLimit; }
    public byte[] srcIpv6Addr() { return srcIpv6Addr; }
    public byte[] dstIpv6Addr() { return dstIpv6Addr; }
    public KaitaiStruct nextHeader() { return nextHeader; }
    public byte[] rest() { return rest; }
    public Ipv6Packet _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
