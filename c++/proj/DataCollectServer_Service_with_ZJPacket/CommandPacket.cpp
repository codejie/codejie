
#include <memory>

#include "CommandPacket.h"

const std::string CommandPacket::CMD_POST       =   "PST:";
const std::string CommandPacket::CMD_ACK        =   "ACK:";

const std::string CommandPacket::CMD_TAG_MN     =   "MN=";
const std::string CommandPacket::CMD_TAG_MFLAG  =   "MFlag=";
const std::string CommandPacket::CMD_TAG_QN     =   "QN=";
const std::string CommandPacket::CMD_TAG_QNRTN  =   "QnRtn=";


int CommandPacket::Analyse(const std::string &stream, CommandPacket *&packet)
{
    if(stream.compare(0, CMD_POST.size(), CMD_POST) == 0)
    {//post
        packet = new CmdPostPacket();
        if(packet->Analyse(stream.substr(CMD_POST.size())) != 0)
        {
            delete packet;
            return -1;
        }
    }
    else
    {//ack
        return -1;
    }

    return 0;
}

int CommandPacket::Make(std::string& stream) const
{
    std::ostringstream ostr;
    ostr << CMD_ACK;

    if(Make(ostr) != 0)
        return -1;

    stream = ostr.str();

    return 0;
}

void CommandPacket::Show(std::ostream &os) const
{
    if(_type == CT_POST)
        os << CMD_POST;
    else
        os << CMD_ACK;
}

///
CmdPostPacket::CmdPostPacket()
: CommandPacket(CommandPacket::CT_POST)
{
}

int CmdPostPacket::Analyse(const std::string &data)
{
    std::string::size_type end = data.find(";");
    std::string::size_type begin = 0;
    std::string::size_type pos = 0;
    while(end == std::string::npos)
    {
        pos = data.substr(begin, end).find("=");
        if(pos == std::string::npos)
            return -1;
        if(data.substr(begin, pos + 1) == CommandPacket::CMD_TAG_MN)
            MN = data.substr(begin + pos + 1, end - begin - pos);
        else if(data.substr(begin, pos + 1) == CommandPacket::CMD_TAG_MFLAG)
            MFlag = data.substr(begin + pos + 1, end - begin - pos);
        else if(data.substr(begin, pos + 1) == CommandPacket::CMD_TAG_QN)
            QN = data.substr(begin + pos + 1, end - begin - pos);
        
        begin = end + 1;
    }

    return 0;
}

void CmdPostPacket::Show(std::ostream &os) const
{
    CommandPacket::Show(os);

    os << "\n" << CommandPacket::CMD_TAG_MN << MN;
    os << "\n" << CommandPacket::CMD_TAG_MFLAG << MFlag;
    os << "\n" << CommandPacket::CMD_TAG_QN << QN << std::endl;

}

///
CmdAckPacket::CmdAckPacket()
: CommandPacket(CommandPacket::CT_ACK)
{
}

int CmdAckPacket::Make(std::ostringstream& ostr) const
{
    ostr << CommandPacket::CMD_TAG_QNRTN << QnRtn << ";";
    return 0;
}

///
std::ostream& operator << (std::ostream& os, const CommandPacket& packet)
{
	packet.Show(os);
	return os;
}