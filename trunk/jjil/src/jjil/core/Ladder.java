/*
 * Ladder.java
 *
 * Created on August 27, 2006, 8:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * Copyright 2006 by Jon A. Webb
 *     This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the Lesser GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package jjil.core;

/** Ladder manages two pipelines. An image is passed to both. The output
 * of each pipeline is passed to a merge function.
 *
 * @author webb
 */
public class Ladder extends PipelineStage {
    /**
     * The Join interface defines a function which combines two images into
     * one. It is used in Ladder to combine the outputs of the two pipelines.
     */
    public interface Join {
        /**
         * Combine the two images and produce an image as output
         * @param imageFirst First image
         * @param imageSecond Second image
         * @return the combined image
         */
        Image DoJoin(Image imageFirst, Image imageSecond);
    }
    
    /**
     * First pipeline
     */
    protected final PipelineStage pipeFirst;
    /**
     * Second pipeline
     */
    protected final PipelineStage pipeSecond;
    /**
     * Join class, which combines the two output images into one.
     */
    protected final Join join;
    
    /**
     * Creates a new instance of Ladder
     * @param pipeFirst First pipeline
     * @param pipeSecond Second pipeline
     * @param join Join class, which combines the two output images into one.
     */
    public Ladder(
            PipelineStage pipeFirst, 
            PipelineStage pipeSecond,
            Join join) {
        this.pipeFirst = pipeFirst;
        this.pipeSecond = pipeSecond;
        this.join = join;
    }
    
    /**
     * Pass the input image to both pipeines, then combine the two outputs into one
     * using the join operation
     * @param image Input image
     * @throws IllegalStateException if either pipeline does not produce an output
     * after being supplied with the input.
     */
    public void Push(Image image) throws IllegalStateException {
        Image imageCopy = image.Clone();
        pipeFirst.Push(image);
        pipeSecond.Push(imageCopy);
        if (pipeFirst.Empty()) {
            throw new IllegalStateException(
                    Messages.getString("Ladder.0") + //$NON-NLS-1$
                    Messages.getString("Ladder.1") + pipeFirst.toString() + //$NON-NLS-1$
                    Messages.getString("Ladder.2")); //$NON-NLS-1$
        }
        if (pipeSecond.Empty()) {
            throw new IllegalStateException(
                    Messages.getString("Ladder.3") + //$NON-NLS-1$
                    Messages.getString("Ladder.4") + pipeSecond.toString() + //$NON-NLS-1$
                    Messages.getString("Ladder.5")); //$NON-NLS-1$
        }
        super.setOutput(
                join.DoJoin(pipeFirst.Front(), pipeSecond.Front())
                );
    }
}